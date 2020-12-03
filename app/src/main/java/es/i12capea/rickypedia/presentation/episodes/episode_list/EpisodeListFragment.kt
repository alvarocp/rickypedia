package es.i12capea.rickypedia.presentation.episodes.episode_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.databinding.FragmentEpisodeListBinding
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.episodes.episode_list.state.EpisodeListStateEvent

@AndroidEntryPoint
class EpisodeListFragment
    : Fragment()
{
    private var _binding: FragmentEpisodeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel : EpisodeListViewModel by activityViewModels()

    lateinit var episodeListAdapter: EpisodeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRecyclerState(binding.rvEpisodes.layoutManager?.onSaveInstanceState())
        _binding = null
    }

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
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.INVISIBLE
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
        binding.rvEpisodes.apply {
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
                binding.rvEpisodes.layoutManager?.onRestoreInstanceState(parcel)
                viewModel.setRecyclerState(null)
            }
        }
    }


}