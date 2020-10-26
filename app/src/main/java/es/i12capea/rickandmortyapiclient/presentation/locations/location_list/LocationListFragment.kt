package es.i12capea.rickandmortyapiclient.presentation.locations.location_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.common.displayToast
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.locations.location_list.state.LocationListStateEvent
import kotlinx.android.synthetic.main.fragment_location_list.*

@AndroidEntryPoint
class LocationListFragment
    : Fragment(R.layout.fragment_location_list)
{
    private val viewModel : LocationListViewModel by activityViewModels()

    lateinit var locationListAdapter : LocationListAdapter

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
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.INVISIBLE
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
        rv_locations.apply {
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
                rv_locations.layoutManager?.onRestoreInstanceState(parcel)
                viewModel.setRecyclerState(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRecyclerState(rv_locations.layoutManager?.onSaveInstanceState())
    }
}