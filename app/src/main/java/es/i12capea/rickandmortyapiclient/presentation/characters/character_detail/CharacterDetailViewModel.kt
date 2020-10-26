package es.i12capea.rickandmortyapiclient.presentation.characters.character_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharacterUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickandmortyapiclient.presentation.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.rickandmortyapiclient.presentation.characters.character_detail.state.CharacterDetailViewState
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.episodeListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch


class CharacterDetailViewModel @ViewModelInject constructor(
    private val getEpisodes: GetEpisodesUseCase,
    private val getCharacter: GetCharacterUseCase
) : BaseViewModel<CharacterDetailStateEvent, CharacterDetailViewState>(){

    override fun setStateEvent(stateEvent: CharacterDetailStateEvent) {
        super.setStateEvent(stateEvent)
    }

    override fun getJobNameForEvent(stateEvent: CharacterDetailStateEvent): String? {
        return when(stateEvent){
            is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                CharacterDetailStateEvent.GetEpisodesFromCharacter::class.java.name
            }
            is CharacterDetailStateEvent.GetCharacter -> {
                CharacterDetailStateEvent.GetCharacter::class.java.name
            }
            else -> null
        }
    }

    override fun getJobForEvent(stateEvent: CharacterDetailStateEvent): Job? {
        return launch {
            when (stateEvent) {
                is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                    getEpisodes(stateEvent.character.episodes)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                            handleCompletion(cause)
                        }
                        .collect {
                            handleCollectEpisodes(it.episodeListToPresentation())
                        }
                }

                is CharacterDetailStateEvent.GetCharacter -> {
                    getCharacter(stateEvent.id)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                            handleCompletion(cause)
                        }
                        .collect {
                            handleCollectCharacter(it.toPresentation())
                        }
                }
            }
        }
    }


    override fun initNewViewState(): CharacterDetailViewState {
        return CharacterDetailViewState()
    }

    private fun handleCollectCharacter(character: Character) {
        dataState.postValue(
            Event(
            CharacterDetailViewState(
                character = character
            )
        )
        )
    }

    fun setImageLoad(isLoad: Boolean){
        dataState.postValue(
            Event(
            CharacterDetailViewState(
                isImageLoaded = isLoad
            )
        )
        )
    }

    private fun handleCollectEpisodes(episodes: List<Episode>) {
        dataState.postValue(
            Event(
                CharacterDetailViewState(
                    episodes = episodes
                )
            )
        )
    }

    fun getEpisodeList() : List<Episode>?{
        return getCurrentViewStateOrNew().episodes
    }

    fun setEpisodeList(episodes: List<Episode>){
        val update = getCurrentViewStateOrNew()
        update.episodes = episodes
        postViewState(update)
    }

    fun setCharacterDetails(character: Character){
        val update = getCurrentViewStateOrNew()
        update.character = character
        postViewState(update)
    }

    fun getCharacterDetails() : Character?{
        return getCurrentViewStateOrNew().character
    }
}
