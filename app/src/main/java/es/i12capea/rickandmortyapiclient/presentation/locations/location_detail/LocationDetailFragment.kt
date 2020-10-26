package es.i12capea.rickandmortyapiclient.presentation.locations.location_detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.character_list.CharacterListAdapterDeepLink
import es.i12capea.rickandmortyapiclient.presentation.common.displayToast
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.locations.location_detail.state.LocationDetailStateEvent
import kotlinx.android.synthetic.main.fragment_location_detail.*
import kotlinx.android.synthetic.main.location_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class LocationDetailFragment constructor()
    : Fragment(R.layout.fragment_location_detail)
{
    private val listViewModel : LocationDetailViewModel by viewModels()

    private val args: LocationDetailFragmentArgs by navArgs()

    lateinit var characterListAdapterDeepLink: CharacterListAdapterDeepLink

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        subscribeObservers()

        initRecyclerView()

        listViewModel.getLocation()?.let { location ->
            listViewModel.getCharactersInLocation()?.let { characters ->
                setLocation(location)
                characterListAdapterDeepLink.submitList(characters)
            }
        } ?: kotlin.run {
            args.location?.let {
                setLocation(it)
                listViewModel.setStateEvent(LocationDetailStateEvent.GetCharactersInLocation(it))
            }
        }


    }

    private fun subscribeObservers() {

        listViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.getContentIfNotHandled()?.let { viewState ->
                viewState.characters?.let {
                    listViewModel.setCharactersInLocation(it)
                }
            }
        })

        listViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading){
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.INVISIBLE
            }
        })

        listViewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                displayToast(it.desc)
            }
        })

        listViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.characters?.let {
                characterListAdapterDeepLink.submitList(it)
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        })
    }

    fun setLocation(it: Location){
        location_item.tv_location_name.text = it.name
        location_item.tv_location_dimension.text = it.dimension
        location_item.tv_location_type.text = it.type

        listViewModel.setLocation(it)
    }

    private fun initRecyclerView() {
        rv_characters_location.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            characterListAdapterDeepLink = CharacterListAdapterDeepLink()
            characterListAdapterDeepLink.setHasStableIds(true)
            adapter = characterListAdapterDeepLink

            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        }
    }
}