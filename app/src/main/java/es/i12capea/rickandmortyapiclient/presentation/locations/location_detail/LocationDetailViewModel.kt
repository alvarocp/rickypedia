package es.i12capea.rickandmortyapiclient.presentation.locations.location_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInLocationUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toDomain
import es.i12capea.rickandmortyapiclient.presentation.locations.location_detail.state.LocationDetailStateEvent
import es.i12capea.rickandmortyapiclient.presentation.locations.location_detail.state.LocationDetailViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class LocationDetailViewModel @ViewModelInject constructor (
    private val getCharactersInLocationUseCase: GetCharactersInLocationUseCase,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<LocationDetailStateEvent, LocationDetailViewState>(dispatcher) {


    override fun getJobNameForEvent(stateEvent: LocationDetailStateEvent): String? {
        return when(stateEvent){
            is LocationDetailStateEvent.GetCharactersInLocation -> {
                LocationDetailStateEvent.GetCharactersInLocation::class.java.name + stateEvent.location.id
            }
        }
    }

    override fun getJobForEvent(stateEvent: LocationDetailStateEvent): Job? {
        return launch {
            when(stateEvent){
                is LocationDetailStateEvent.GetCharactersInLocation -> {
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

    override fun initNewViewState(): LocationDetailViewState {
        return LocationDetailViewState()
    }

    private fun handleCollectCharacters(characters: List<Character>) {
        dataState.postValue(
            Event(
                LocationDetailViewState(
                    characters = characters
                )
            )
        )
    }

    fun setLocation(location: Location){
        val update = getCurrentViewStateOrNew()
        update.location = location
        postViewState(update)
    }

    fun getLocation() : Location?{
        return getCurrentViewStateOrNew().location
    }

    fun setCharactersInLocation(characters: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = characters
        postViewState(update)
    }

    fun getCharactersInLocation() : List<Character>? {
        return getCurrentViewStateOrNew().characters
    }


}
