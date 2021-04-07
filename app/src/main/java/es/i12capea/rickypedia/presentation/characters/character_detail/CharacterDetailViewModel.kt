package es.i12capea.rickypedia.presentation.characters.character_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickypedia.common.Event
import es.i12capea.domain.usecases.GetCharacterUseCase
import es.i12capea.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickypedia.presentation.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.rickypedia.presentation.characters.character_detail.state.CharacterDetailViewState
import es.i12capea.rickypedia.presentation.common.BaseViewModel
import es.i12capea.rickypedia.presentation.entities.Character
import es.i12capea.rickypedia.presentation.entities.Episode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class CharacterDetailViewModel @ViewModelInject constructor(
    private val getCharacter: GetCharacterUseCase,
    private val getEpisodes: GetEpisodesUseCase,
    private val dispatcher: CoroutineDispatcher
    ) : BaseViewModel<CharacterDetailStateEvent, CharacterDetailViewState>(dispatcher){


    override fun getJobNameForEvent(stateEvent: CharacterDetailStateEvent): String? {
        return when(stateEvent){
            is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                CharacterDetailStateEvent.GetEpisodesFromCharacter::class.java.name
            }
            is CharacterDetailStateEvent.GetCharacter -> {
                CharacterDetailStateEvent.GetCharacter::class.java.name
            }
        }
    }

    override fun getJobForEvent(stateEvent: CharacterDetailStateEvent): Job? {
        return launch {
            when (stateEvent) {
                is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                    try {
                        getEpisodes(stateEvent.character.episodes)
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectEpisodes(it.episodeListToPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
                    }
                }

                is CharacterDetailStateEvent.GetCharacter -> {
                    try {
                        getCharacter(stateEvent.id)
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectCharacter(it.toPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
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
