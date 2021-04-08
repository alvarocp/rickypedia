package es.i12capea.rickypedia.features.episodes.episode_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.mappers.characterListToPresentation
import es.i12capea.rickypedia.entities.mappers.toDomain
import es.i12capea.rickypedia.entities.mappers.toPresentation
import es.i12capea.rickypedia.features.episodes.episode_detail.state.EpisodeDetailStateEvent
import es.i12capea.rickypedia.features.episodes.episode_detail.state.EpisodeDetailViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EpisodeDetailViewModel @ViewModelInject constructor(
    private val getCharactersInEpisodeUseCase: GetCharactersInEpisodeUseCase,
    private val getEpisodeUseCase: GetEpisodeUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel<EpisodeDetailStateEvent, EpisodeDetailViewState>(dispatcher){


    override fun getJobNameForEvent(stateEvent: EpisodeDetailStateEvent): String? {
        return when(stateEvent){
            is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                EpisodeDetailStateEvent.GetCharactersInEpisode::class.java.name + stateEvent.episode.id
            }
            is EpisodeDetailStateEvent.GetEpisode -> {
                EpisodeDetailStateEvent.GetEpisode::class.java.name + stateEvent.episodeId
            }
        }
    }

    override fun getJobForEvent(stateEvent: EpisodeDetailStateEvent): Job? {
        return launch {
            when(stateEvent){
                is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                    try {
                        getCharactersInEpisodeUseCase.invoke(stateEvent.episode.toDomain())
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectCharacters(it.characterListToPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
                    }
                }
                is EpisodeDetailStateEvent.GetEpisode -> {
                    try {
                        getEpisodeUseCase.invoke(stateEvent.episodeId)
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectEpisode(it.toPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
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
        launch { setViewState(update) }
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
        launch { setViewState(update) }

    }

    fun getCurrentEpisode() : Episode?{
        return getCurrentViewStateOrNew().episode
    }



}