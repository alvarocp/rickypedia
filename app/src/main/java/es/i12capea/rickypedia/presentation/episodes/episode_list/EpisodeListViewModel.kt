package es.i12capea.rickypedia.presentation.episodes.episode_list

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickypedia.domain.usecases.GetEpisodesInPageUseCase
import es.i12capea.rickypedia.domain.usecases.GetCharactersInEpisodeUseCase
import es.i12capea.rickypedia.domain.usecases.GetEpisodeUseCase
import es.i12capea.rickypedia.presentation.common.BaseViewModel
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.entities.Page
import es.i12capea.rickypedia.presentation.entities.mappers.episodePageToPresentation
import es.i12capea.rickypedia.presentation.entities.mappers.locationPageEntityToPresentation
import es.i12capea.rickypedia.presentation.episodes.episode_list.state.EpisodeListStateEvent
import es.i12capea.rickypedia.presentation.episodes.episode_list.state.EpisodeListViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class EpisodeListViewModel @ViewModelInject constructor(
    private val getEpisodesInPageUseCase: GetEpisodesInPageUseCase,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<EpisodeListStateEvent, EpisodeListViewState>(dispatcher){


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

    override fun getJobNameForEvent(stateEvent: EpisodeListStateEvent): String? {
        return when(stateEvent){
            is EpisodeListStateEvent.GetNextPage -> {
                EpisodeListStateEvent.GetNextPage::class.java.name + getNextPage()
            }
        }
    }

    override fun getJobForEvent(stateEvent: EpisodeListStateEvent): Job? {
        return launch {
            when(stateEvent){
                is EpisodeListStateEvent.GetNextPage -> {
                    getLastPage()?.let { currentPage ->
                        currentPage.next?.let { nextPage ->
                            getNextEpisodePageFlow(nextPage)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getNextEpisodePageFlow(nextPage: Int){
        val currentEpisodes = getCurrentEpisodes()
        try {
            getEpisodesInPageUseCase.invoke(nextPage)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleCollectEpisodes(currentEpisodes, it.episodePageToPresentation())
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }
    }

    private fun handleCollectEpisodes(currentList: List<Episode>? , page: Page<Episode>) {

        setActualEpisodePage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        setEpisodeList(list)
    }

    fun setActualEpisodePage(page: Page<Episode>){
        val update = getCurrentViewStateOrNew()
        update.lastPage = page
        postViewState(update)
    }


    override fun initNewViewState(): EpisodeListViewState {
        return EpisodeListViewState()
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

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = state
        postViewState(update)
    }

    fun getRecyclerState() : Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }
}