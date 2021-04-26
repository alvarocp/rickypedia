package es.i12capea.rickypedia.features.locations.location_list

import android.os.Parcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import es.i12capea.domain.usecases.GetLocationsInPageUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Location
import es.i12capea.rickypedia.entities.Page
import es.i12capea.rickypedia.entities.mappers.locationPageEntityToPresentation
import es.i12capea.rickypedia.features.locations.location_list.state.LocationListStateEvent
import es.i12capea.rickypedia.features.locations.location_list.state.LocationListViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor (
    private val getLocations: GetLocationsInPageUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel<LocationListStateEvent, LocationListViewState>(dispatcher) {

    override fun getJobNameForEvent(stateEvent: LocationListStateEvent): String {
        return when(stateEvent){
            is LocationListStateEvent.GetNextLocationPage -> {
                LocationListStateEvent.GetNextLocationPage::class.java.name + getNextPage()
            }
        }
    }

    override fun getJobForEvent(stateEvent: LocationListStateEvent): Job {
        return launch {
            when(stateEvent){
                is LocationListStateEvent.GetNextLocationPage -> {
                    getLastPage()?.let { currentPage ->
                        currentPage.next?.let { nextPage ->
                            getNextLocationPageFlow(nextPage)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getNextLocationPageFlow(nextPage: Int){
        val currentCharacters = getLocations()
        try {
            getLocations.invoke(nextPage)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleCollectLocations(currentCharacters, it.locationPageEntityToPresentation())
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }
    }

    private suspend fun handleCollectLocations(currentLocations: List<Location>?, page: Page<Location>) {
        setLastPage(page)

        val list = currentLocations?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        setLocationList(list)
    }

    override fun initNewViewState(): LocationListViewState {
        return LocationListViewState(
                lastPage = Page(
                        next = 1,
                        prev = null,
                        actual = 0,
                        list = emptyList(),
                        count = 0
                )
        )
    }

    fun getLocations() : List<Location>?{
        return getCurrentViewState().locations
    }

    private suspend fun setLocationList(locations: List<Location>){
        val update = getCurrentViewState()
        setViewState(update.copy(locations = locations))
    }

    private suspend fun setLastPage(page: Page<Location>) {
        val update = getCurrentViewState()
        setViewState(update.copy(lastPage = page))
    }

    fun getLastPage() : Page<Location>? {
        return getCurrentViewState().lastPage
    }

    private fun getNextPage() : Int?{
        return getCurrentViewState().lastPage?.next
    }

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(layoutManagerState = state)) }
    }

    fun getRecyclerState() : Parcelable? {
        return getCurrentViewState().layoutManagerState
    }

    override fun setLoading(isLoading: Boolean): LocationListViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): LocationListViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }
}