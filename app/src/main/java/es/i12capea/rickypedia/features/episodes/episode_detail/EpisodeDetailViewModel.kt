package es.i12capea.rickypedia.features.episodes.episode_detail

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.mappers.characterListToPresentation
import es.i12capea.rickypedia.entities.mappers.toDomain
import es.i12capea.rickypedia.entities.mappers.toPresentation
import es.i12capea.rickypedia.features.episodes.episode_detail.state.EpisodeDetailStateEvent
import es.i12capea.rickypedia.features.episodes.episode_detail.state.EpisodeDetailViewState
import es.i12capea.rickypedia.features.locations.location_list.state.LocationListViewState
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


    override fun getJobNameForEvent(stateEvent: EpisodeDetailStateEvent): String {
        return when(stateEvent){
            is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                EpisodeDetailStateEvent.GetCharactersInEpisode::class.java.name + stateEvent.episode.id
            }
            is EpisodeDetailStateEvent.GetEpisodeAndCharactersInEpisode -> {
                EpisodeDetailStateEvent.GetEpisodeAndCharactersInEpisode::class.java.name + stateEvent.episodeId
            }
        }
    }

    override fun getJobForEvent(stateEvent: EpisodeDetailStateEvent): Job {
        return launch {
            when(stateEvent){
                is EpisodeDetailStateEvent.GetCharactersInEpisode -> {
                    invokeGetCharactersInEpisodeUseCase(stateEvent.episode)
                }
                is EpisodeDetailStateEvent.GetEpisodeAndCharactersInEpisode -> {
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

    private suspend fun invokeGetCharactersInEpisodeUseCase(episode: Episode){
        try {
            getCharactersInEpisodeUseCase.invoke(episode.toDomain())
                    .flowOn(Dispatchers.IO)
                    .collect {
                        handleCollectCharacters(it.characterListToPresentation())
                    }
        }catch (t: Throwable){
            handleThrowable(t)
        }
    }

    private suspend fun handleCollectEpisode(episode: Episode) {
        val update = getCurrentViewState()
        setViewState(update.copy(episode = episode))
        invokeGetCharactersInEpisodeUseCase(episode)
    }

    private suspend fun handleCollectCharacters(characters: List<Character>) {
        setCharacterList(characters)
    }

    private suspend fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(characters = cl)) }
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewState().characters
    }

    override fun initNewViewState(): EpisodeDetailViewState {
        return EpisodeDetailViewState()
    }

    fun setCurrentEpisode(episode: Episode){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(episode = episode)) }
    }

    fun getCurrentEpisode() : Episode?{
        return getCurrentViewState().episode
    }

    override fun setLoading(isLoading: Boolean): EpisodeDetailViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): EpisodeDetailViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }

}