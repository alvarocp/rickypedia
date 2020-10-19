package es.i12capea.rickandmortyapiclient.presentation.episodes

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllEpisodesUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.*
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


    init {
        val update = getCurrentViewStateOrNew()
        update.lastPage = Page(
            next = 1,
            prev = null,
            actual = 0,
            list = emptyList(),
            count = 0
        )
        update.episodes = null
        setViewState(update)
    }

    override fun getJobNameForEvent(stateEvent: EpisodesStateEvent): String? {
        return when(stateEvent){
            is EpisodesStateEvent.GetNextPage -> {
                EpisodesStateEvent.GetNextPage::class.java.name + getNextPage()
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
                is EpisodesStateEvent.GetNextPage -> {
                    val currentEpisodes = getCurrentEpisodes()
                    getLastPage()?.let { currentPage ->
                        currentPage.next?.let { nextPage ->
                            getAllEpisodesUseCase.invoke(nextPage)
                                .flowOn(Dispatchers.IO)
                                .onCompletion { cause ->
                                    handleCompletion(cause)
                                }
                                .collect{
                                    handleCollectEpisodes(currentEpisodes, it.episodePageToPresentation())
                                }
                        }
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

    private fun handleCollectEpisodes(currentList: List<Episode>? ,page: Page<Episode>) {

        setActualEpisodePage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        setEpisodeList(list)
    }


    fun getEpisodeList() : List<Episode>?{
        return getCurrentViewStateOrNew().episodes
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

    fun getActualPage() : Page<Episode>?{
        return getCurrentViewStateOrNew().lastPage
    }

    fun setActualEpisodePage(page: Page<Episode>){
        val update = getCurrentViewStateOrNew()
        update.lastPage = page
        postViewState(update)
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewStateOrNew().characters
    }

    override fun initNewViewState(): EpisodesViewState {
        return EpisodesViewState()
    }

    fun setCurrentEpisode(episode: Episode){
        val update = getCurrentViewStateOrNew()
        update.episode = episode
        postViewState(update)
    }

    fun getCurrentEpisode() : Episode?{
        return getCurrentViewStateOrNew().episode
    }

    fun getCurrentEpisodes() : List<Episode>?{
        return getCurrentViewStateOrNew().episodes
    }

    fun setEpisodeList(episodes: List<Episode>){
        val update = getCurrentViewStateOrNew()
        update.episodes = episodes
        postViewState(update)
    }

    fun getNextPage() : Int? {
        return getCurrentViewStateOrNew().lastPage?.next
    }
    fun getLastPage() : Page<Episode>?{
        return getCurrentViewStateOrNew().lastPage
    }

}