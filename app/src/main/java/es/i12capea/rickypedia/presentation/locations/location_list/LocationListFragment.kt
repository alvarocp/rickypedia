package es.i12capea.rickypedia.presentation.locations.location_list

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
import es.i12capea.rickypedia.databinding.FragmentLocationListBinding
import es.i12capea.rickypedia.presentation.common.displayToast
import es.i12capea.rickypedia.presentation.entities.Location
import es.i12capea.rickypedia.presentation.locations.location_list.state.LocationListStateEvent

@AndroidEntryPoint
class LocationListFragment
    : Fragment()
{
    private var _binding: FragmentLocationListBinding? = null
    private val binding get() = _binding!!

    private val viewModel : LocationListViewModel by activityViewModels()

    lateinit var locationListAdapter : LocationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRecyclerState(binding.rvLocations.layoutManager?.onSaveInstanceState())
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        subscribeObservers()

        viewModel.getLocations()?.let {
            handleLocationList(it)
        } ?: viewModel.setStateEvent(LocationListStateEvent.GetNextLocationPage())

    }

    private fun subscribeObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.INVISIBLE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                displayToast(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.locations?.let {
                locationListAdapter.submitList(it)
            }
        })
    }

    private fun initRecyclerView(){
        binding.rvLocations.apply {
            layoutManager = LinearLayoutManager(this@LocationListFragment.context)
            locationListAdapter = LocationListAdapter()
            locationListAdapter.setHasStableIds(true)
            adapter = locationListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition >= locationListAdapter.itemCount.minus(4)) {
                        viewModel.setStateEvent(LocationListStateEvent.GetNextLocationPage())
                        Log.d("A", "LastPositionReached")
                    }
                }
            })
        }
    }

    private fun handleLocationList(list: List<Location>){
        viewModel.getLastPage()?.let { actualPage ->
            viewModel.getRecyclerState()?.let { parcel ->
                locationListAdapter.submitList(list)
                binding.rvLocations.layoutManager?.onRestoreInstanceState(parcel)
                viewModel.setRecyclerState(null)
            }
        }
    }

}