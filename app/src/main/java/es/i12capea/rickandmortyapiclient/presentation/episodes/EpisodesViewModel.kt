package es.i12capea.rickandmortyapiclient.presentation.episodes

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllEpisodesUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.episodeListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toDomain
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toPresentation
import es.i12capea.rickandmortyapiclient.presentation.episodes.state.EpisodesStateEvent
import es.i12capea.rickandmortyapiclient.presentation.episodes.state.EpisodesViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class EpisodesViewModel @ViewModelInject constructor(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase,
    private val getCharactersInEpisodeUseCase: GetCharactersInEpisodeUseCase,
    private val getEpisodeUseCase: GetEpisodeUseCase
) : BaseViewModel<EpisodesStateEvent, EpisodesViewState>(){


    override fun getJobNameForEvent(stateEvent: EpisodesStateEvent): String? {
        return when(stateEvent){
            is EpisodesStateEvent.GetAllEpisodes -> {
                EpisodesStateEvent.GetAllEpisodes::class.java.name + stateEvent.page
            }
            is EpisodesStateEvent.GetCharactersInEpisode -> {
                EpisodesStateEvent.GetCharactersInEpisode::class.java.name + stateEvent.episode.id
            }
            is EpisodesStateEvent.GetEpisode -> {
                EpisodesStateEvent.GetEpisode::class.java.name + stateEvent.episodeId
            }
            else -> {null}
        }
    }

    override fun getJobForEvent(stateEvent: EpisodesStateEvent): Job? {
        return launch {
            when(stateEvent){
                is EpisodesStateEvent.GetAllEpisodes -> {
                    getAllEpisodesUseCase.invoke(stateEvent.page)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                            handleCompletion(cause)
                        }
                        .collect{
                            handleCollectEpisodes(it.episodeListToPresentation())
                        }
                }
                is EpisodesStateEvent.GetCharactersInEpisode -> {
                    getCharactersInEpisodeUseCase.invoke(stateEvent.episode.toDomain())
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                                handleCompletion(cause)
                        }
                        .collect {
                            handleCollectCharacters(it.characterListToPresentation())
                        }
                }
                is EpisodesStateEvent.GetEpisode -> {
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
                EpisodesViewState(
                    episode = episode
                )
            )
        )
    }

    private fun handleCollectCharacters(characters: List<Character>) {
        dataState.postValue(
            Event(
                EpisodesViewState(
                    characters = characters
                )
            )
        )
    }

    private fun handleCollectEpisodes(episodeList: List<Episode>) {
        dataState.postValue(
            Event(
                EpisodesViewState(
                    episodes = episodeList
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

    fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = cl
        postViewState(update)
    }

    fun getActualPage() : Int{
        return getCurrentViewStateOrNew().page
    }

    fun setActualPage(page: Int){
        val update = getCurrentViewStateOrNew()
        update.page = page
        postViewState(update)
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewStateOrNew().characters
    }

    fun addToEpisodeList(cl: List<Episode>){
        val list = getEpisodeList()?.toMutableList()
            ?: arrayListOf()
        list.addAll(cl)
        val update = getCurrentViewStateOrNew()
        update.episodes = list
        postViewState(update)
    }

    fun handleCompletion(cause: Throwable?, page: Int){
        cause?.let {
            handleError(it)
        }
        setActualPage(page)
    }


    override fun initNewViewState(): EpisodesViewState {
        return EpisodesViewState()
    }

    fun setEpisode(episode: Episode){
        val update = getCurrentViewStateOrNew()
        update.episode = episode
        postViewState(update)
    }

    fun getEpisode() : Episode?{
        return getCurrentViewStateOrNew().episode
    }
}