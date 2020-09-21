package es.i12capea.rickandmortyapiclient.presentation.episodes

import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.DataState
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllEpisodesUseCase
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.episodeListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.episodes.state.EpisodesStateEvent
import es.i12capea.rickandmortyapiclient.presentation.episodes.state.EpisodesViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class EpisodesViewModel @ViewModelInject constructor(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase
) : BaseViewModel<EpisodesStateEvent, EpisodesViewState>(){

    @ExperimentalCoroutinesApi
    override fun setStateEvent(stateEvent: EpisodesStateEvent)  {
        cancelActiveJobs()
        var jobName = ""
        val job = launch {
            dataState.postValue(DataState.loading(true))

            when(stateEvent){
                is EpisodesStateEvent.GetAllEpisodes -> {
                    jobName = EpisodesStateEvent.GetAllEpisodes::class.java.name
                    getAllEpisodesUseCase.invoke(stateEvent.page)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause -> withContext(Dispatchers.Main){
                            handleCompletion(cause)
                        } }
                        .collect{
                            handleCollectEpisodes(it.episodeListToPresentation())
                        }
                }
            }
        }

        addJob(jobName, job)
    }

    private fun handleCollectEpisodes(episodeList: List<Episode>) {
        dataState.postValue(
            DataState.success(
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
        setViewState(update)
    }

    fun setCharacterDetails(character: Character){
        val update = getCurrentViewStateOrNew()
        update.character = character
        setViewState(update)
    }

    fun getCharacterDetails() : Character?{
        return getCurrentViewStateOrNew().character
    }

    fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = cl
        setViewState(update)
    }

    fun getActualPage() : Int{
        return getCurrentViewStateOrNew().page
    }

    fun setActualPage(page: Int){
        val update = getCurrentViewStateOrNew()
        update.page = page
        setViewState(update)
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
        setViewState(update)
    }

    fun handleCompletion(cause: Throwable?, page: Int){
        cause?.let {
            handleError(it)
        }
        setActualPage(page)
        dataState.postValue(DataState.loading(false))
    }


    override fun initNewViewState(): EpisodesViewState {
        return EpisodesViewState()
    }
}