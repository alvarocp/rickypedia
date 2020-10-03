package es.i12capea.rickandmortyapiclient.presentation.locations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.common.displayToast
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.locations.state.LocationStateEvent
import kotlinx.android.synthetic.main.fragment_location_list.*

@AndroidEntryPoint
class LocationListFragment : Fragment() ,
    LocationListAdapter.Interaction
{

    private val viewModel : LocationViewModel by viewModels()

    lateinit var locationListAdapter: LocationListAdapter

    override fun onItemSelected(position: Int, item: Location) {
        findNavController().navigate(R.id.action_locationListFragment_to_locationDetailFragment, bundleOf(Pair("location", item)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        subscribeObservers()

        viewModel.getLocations()?.let {

        } ?: viewModel.setStateEvent(LocationStateEvent.GetNextLocationPage())

    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { viewState ->
                viewState.lastPage?.let {
                    viewModel.setActualPage(it)
                    viewModel.addToLocationList(it.list)
                }
            }
            dataState.loading.let { isLoading ->
                if (isLoading){
                    progress_bar.visibility = View.VISIBLE
                }else{
                    progress_bar.visibility = View.INVISIBLE
                }
            }
            dataState.error?.let {
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
            locationListAdapter = LocationListAdapter(this@LocationListFragment)
            adapter = locationListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == locationListAdapter.itemCount.minus(1)) {
                        viewModel.setStateEvent(LocationStateEvent.GetNextLocationPage( ))
                        Log.d("A", "LastPositionReached")
                    }
                }
            })
        }
    }
}