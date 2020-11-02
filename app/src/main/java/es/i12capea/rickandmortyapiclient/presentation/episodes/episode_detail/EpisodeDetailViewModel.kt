package es.i12capea.rickandmortyapiclient.presentation.episodes.episode_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toDomain
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toPresentation
import es.i12capea.rickandmortyapiclient.presentation.episodes.episode_detail.state.EpisodeDetailStateEvent
import es.i12capea.rickandmortyapiclient.presentation.episodes.episode_detail.state.EpisodeDetailViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class EpisodeDetailViewModel @ViewModelInject constructor(
    private val getCharactersInEpisodeUseCase: GetCharactersInEpisodeUseCase,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<EpisodeDetailStateEvent, EpisodeDetailViewState>(dispatcher){


    override fun getJobNameForEvent(stateEvent: EpisodeDetailStateEvent): String? {
        return when(stateEvent){
            is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                EpisodeDetailStateEvent.GetCharactersInEpisode::class.java.name + stateEvent.episode.id
            }
            is EpisodeDetailStateEvent.GetEpisode -> {
                EpisodeDetailStateEvent.GetEpisode::class.java.name + stateEvent.episodeId
            }
            else -> {null}
        }
    }

    override fun getJobForEvent(stateEvent: EpisodeDetailStateEvent): Job? {
        return launch {
            when(stateEvent){
                is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                    getCharactersInEpisodeUseCase.invoke(stateEvent.episode.toDomain())
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                                handleCompletion(cause)
                        }
                        .collect {
                            handleCollectCharacters(it.characterListToPresentation())
                        }
                }
                is EpisodeDetailStateEvent.GetEpisode -> {
                    getEpisodeUseCase.invoke(stateEvent.episodeId)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                                handleCompletion(cause)
                            }
                        .collect {
                            handleCollectEpisode(it.toPresentation())
                        }
                }
            }
        }
    }



    private fun handleCollectEpisode(episode: Episode) {
        dataState.postValue(
            Event(
                EpisodeDetailViewState(
                    episode = episode
                )
            )
        )
    }

    private fun handleCollectCharacters(characters: List<Character>) {
        dataState.postValue(
            Event(
                EpisodeDetailViewState(
                    characters = characters
                )
            )
        )
    }

    fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = cl
        postViewState(update)
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewStateOrNew().characters
    }

    override fun initNewViewState(): EpisodeDetailViewState {
        return EpisodeDetailViewState()
    }

    fun setCurrentEpisode(episode: Episode){
        val update = getCurrentViewStateOrNew()
        update.episode = episode
        postViewState(update)
    }

    fun getCurrentEpisode() : Episode?{
        return getCurrentViewStateOrNew().episode
    }



}