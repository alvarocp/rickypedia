package es.i12capea.rickypedia.features.locations.location_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Location
import es.i12capea.rickypedia.entities.mappers.characterListToPresentation
import es.i12capea.rickypedia.entities.mappers.toDomain
import es.i12capea.rickypedia.features.locations.location_detail.state.LocationDetailStateEvent
import es.i12capea.rickypedia.features.locations.location_detail.state.LocationDetailViewState
import es.i12capea.rickypedia.features.locations.location_list.state.LocationListViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class LocationDetailViewModel @ViewModelInject constructor (
    private val getCharactersInLocationUseCase: GetCharactersInLocationUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel<LocationDetailStateEvent, LocationDetailViewState>(dispatcher) {


    override fun getJobNameForEvent(stateEvent: LocationDetailStateEvent): String {
        return when(stateEvent){
            is LocationDetailStateEvent.GetCharactersInLocation -> {
                LocationDetailStateEvent.GetCharactersInLocation::class.java.name + stateEvent.location.id
            }
        }
    }

    override fun getJobForEvent(stateEvent: LocationDetailStateEvent): Job {
        return launch {
            when(stateEvent){
                is LocationDetailStateEvent.GetCharactersInLocation -> {
                    try {
                        getCharactersInLocationUseCase.invoke(stateEvent.location.toDomain())
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectCharacters(it.characterListToPresentation())
                            }
                    } catch (t: Throwable){
                        handleThrowable(t)
                    }
                }
            }
        }
    }

    override fun initNewViewState(): LocationDetailViewState {
        return LocationDetailViewState()
    }

    private suspend fun handleCollectCharacters(characters: List<Character>) {
        setCharactersInLocation(characters)
    }

    fun setLocation(location: Location){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(location = location)) }
    }

    fun getLocation() : Location?{
        return getCurrentViewState().location
    }

    suspend fun setCharactersInLocation(characters: List<Character>){
        val update = getCurrentViewState()
        setViewState(update.copy(characters = characters))
    }

    fun getCharactersInLocation() : List<Character>? {
        return getCurrentViewState().characters
    }

    override fun setLoading(isLoading: Boolean): LocationDetailViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): LocationDetailViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }
}
