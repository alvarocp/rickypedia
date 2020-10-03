package es.i12capea.rickandmortyapiclient.presentation.locations

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.DataState
import es.i12capea.rickandmortyapiclient.domain.usecases.GetLocationsInPage
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.locationPageEntityToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toDomain
import es.i12capea.rickandmortyapiclient.presentation.locations.state.LocationStateEvent
import es.i12capea.rickandmortyapiclient.presentation.locations.state.LocationViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class LocationViewModel @ViewModelInject constructor (
    private val getLocationsInPage: GetLocationsInPage,
    private val getCharactersInLocationUseCase: GetCharactersInLocationUseCase
) : BaseViewModel<LocationStateEvent, LocationViewState>() {

    override fun getJobNameForEvent(stateEvent: LocationStateEvent): String? {
        return when(stateEvent){
            is LocationStateEvent.GetNextLocationPage -> {
                LocationStateEvent.GetNextLocationPage::class.java.name + getNextPage()
            }
            is LocationStateEvent.GetCharactersInLocation -> {
                LocationStateEvent.GetCharactersInLocation::class.java.name + stateEvent.location.id
            }
        }
    }

    override fun getJobForEvent(stateEvent: LocationStateEvent): Job? {
        return launch {
            when(stateEvent){
                is LocationStateEvent.GetNextLocationPage -> {
                    try {
                        getNextPage()?.let { nextPage ->
                            getLocationsInPage.invoke(nextPage)
                                .flowOn(Dispatchers.IO)
                                .onCompletion { cause ->
                                    handleCompletion(cause)
                                }
                                .collect{
                                    handleCollectEpisodes(it.locationPageEntityToPresentation())
                                }
                        }
                    }catch (t: Throwable){
                        handleError(t)
                    }
                }

                is LocationStateEvent.GetCharactersInLocation -> {
                    try {
                        getCharactersInLocationUseCase.invoke(stateEvent.location.toDomain())
                            .flowOn(Dispatchers.IO)
                            .onCompletion { cause ->
                                handleCompletion(cause)
                            }
                            .collect {
                                handleCollectCharacters(it.characterListToPresentation())
                            }
                    } catch (t: Throwable){
                        handleError(t)
                    }
                }
            }
        }
    }


    private fun handleCollectCharacters(characters: List<Character>) {
        dataState.postValue(
            DataState.success(
                LocationViewState(
                    characters = characters
                )
            )
        )
    }

    private fun handleCollectEpisodes(page: Page<Location>) {
        dataState.postValue(
            DataState.success(
                LocationViewState(
                    lastPage = page
                )
            )
        )
    }

    private fun handleCompletion(cause: Throwable?, page: Page<Location>){
        setActualPage(page)
        handleCompletion(cause)
    }

    override fun initNewViewState(): LocationViewState {
        return LocationViewState()
    }

    fun getLocations() : List<Location>?{
        return getCurrentViewStateOrNew().locations
    }

    fun setLocations(locations: List<Location>){
        val update = getCurrentViewStateOrNew()
        update.locations = locations
        setViewState(update)
    }

    fun setActualPage(page: Page<Location>) {
        val update = getCurrentViewStateOrNew()
        update.lastPage = page
        setViewState(update)
    }

    fun getActualPage() : Page<Location>? {
        return getCurrentViewStateOrNew().lastPage
    }

    fun addToLocationList(locations: List<Location>) {
        val list = getLocations()?.toMutableList()
            ?: arrayListOf()
        list.addAll(locations)
        val update = getCurrentViewStateOrNew()
        update.locations = list
        setViewState(update)
    }

    fun setCharactersInLocation(characters: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = characters
        setViewState(update)
    }

    fun getCharactersInLocation() : List<Character>? {
        return getCurrentViewStateOrNew().characters
    }

    fun getNextPage() : Int?{
        return getCurrentViewStateOrNew().lastPage?.next
    }

    fun getPrevPage() : Int?{
        return getCurrentViewStateOrNew().lastPage?.prev
    }

}