package es.i12capea.rickypedia.features.episodes.episode_list

import android.os.Parcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import es.i12capea.rickypedia.shared.domain.usecases.GetEpisodesInPageUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.Page
import es.i12capea.rickypedia.entities.mappers.episodePageToPresentation
import es.i12capea.rickypedia.features.episodes.episode_list.state.EpisodeListStateEvent
import es.i12capea.rickypedia.features.episodes.episode_list.state.EpisodeListViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeListViewModel @Inject constructor(
    private val getEpisodesInPageUseCase: GetEpisodesInPageUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel<EpisodeListStateEvent, EpisodeListViewState>(dispatcher){

    override fun getJobNameForEvent(stateEvent: EpisodeListStateEvent): String {
        return when(stateEvent){
            is EpisodeListStateEvent.GetNextPage -> {
                EpisodeListStateEvent.GetNextPage::class.java.name + getNextPage()
            }
        }
    }

    override fun getJobForEvent(stateEvent: EpisodeListStateEvent): Job {
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

    private suspend fun handleCollectEpisodes(currentList: List<Episode>?, page: Page<Episode>) {
        setActualEpisodePage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        setEpisodeList(list)
    }

    private suspend fun setActualEpisodePage(page: Page<Episode>){
        val update = getCurrentViewState()
        setViewState(update.copy(lastPage = page))
    }


    override fun initNewViewState(): EpisodeListViewState {
        return EpisodeListViewState(
            lastPage = Page(
                next = 1,
                prev = null,
                actual = 0,
                list = emptyList(),
                count = 0
            )
        )
    }

    fun getCurrentEpisodes() : List<Episode>?{
        return getCurrentViewState().episodes
    }

    private suspend fun setEpisodeList(episodes: List<Episode>){
        val update = getCurrentViewState()
        setViewState(update.copy(episodes = episodes))
    }

    private fun getNextPage() : Int? {
        return getCurrentViewState().lastPage?.next
    }
    fun getLastPage() : Page<Episode>?{
        return getCurrentViewState().lastPage
    }

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(layoutManagerState = state)) }
    }

    fun getRecyclerState() : Parcelable? {
        return getCurrentViewState().layoutManagerState
    }

    override fun setLoading(isLoading: Boolean): EpisodeListViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): EpisodeListViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }
}