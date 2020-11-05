package es.i12capea.rickypedia.presentation.episodes.episode_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.episodes.episode_list.state.EpisodeListStateEvent
import kotlinx.android.synthetic.main.episode_list_fragment.*

@AndroidEntryPoint
class EpisodeListFragment
    : Fragment(R.layout.episode_list_fragment){

    private val viewModel : EpisodeListViewModel by activityViewModels()

    lateinit var episodeListAdapter: EpisodeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        subscribeObservers()

        viewModel.getCurrentEpisodes()?.let {
            handleEpisodes(it)
        } ?: viewModel.setStateEvent(EpisodeListStateEvent.GetNextPage())

    }


    fun subscribeObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if(it){
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.INVISIBLE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                this.displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.episodes?.let {
                episodeListAdapter.submitList(it)
            }
        })
    }

    private fun initRecyclerView(){
        rv_episodes.apply {
            layoutManager = LinearLayoutManager(this@EpisodeListFragment.context)
            episodeListAdapter = EpisodeListAdapter()
            episodeListAdapter.setHasStableIds(true)
            adapter = episodeListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == episodeListAdapter.itemCount.minus(1)) {
                        viewModel.setStateEvent(EpisodeListStateEvent.GetNextPage())
                        Log.d("A", "LastPositionReached")
                    }
                }
            })
        }
    }

    private fun handleEpisodes(list: List<Episode>){
        viewModel.getLastPage()?.let { actualPage ->
            viewModel.getRecyclerState()?.let { parcel ->
                episodeListAdapter.submitList(list)
                rv_episodes.layoutManager?.onRestoreInstanceState(parcel)
                viewModel.setRecyclerState(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRecyclerState(rv_episodes.layoutManager?.onSaveInstanceState())
    }
}