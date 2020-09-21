package es.i12capea.rickandmortyapiclient.presentation.locations

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.CharacterListAdapter
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.locations.state.LocationStateEvent
import kotlinx.android.synthetic.main.fragment_location_detail.*
import kotlinx.android.synthetic.main.location_item.view.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationDetailFragment : Fragment(),
CharacterListAdapter.Interaction
{

    override fun onItemSelected(position: Int, item: Character, imageView: ImageView) {
        findNavController().navigate( Uri.parse("https://www.rickandmortyapiclient.com/character/${item.id}"))
    }

    @Inject
    lateinit var requestManager: RequestManager

    private val args: LocationDetailFragmentArgs by navArgs()
    private val viewModel : LocationViewModel by  activityViewModels()

    lateinit var characterListAdapter: CharacterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        initRecyclerView()

        args.location?.let {
            location_item.tv_location_name.text = it.name
            location_item.tv_location_dimension.text = it.dimension
            location_item.tv_location_type.text = it.type

            viewModel.setStateEvent(LocationStateEvent.GetCharactersInLocation(it))
        }


    }

    private fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { viewState ->
                viewState.characters?.let {
                    viewModel.setCharactersInLocation(it)
                    characterListAdapter.submitList(it)
                }
            }
            dataState.loading.let {

            }
            dataState.error?.let {

            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

        })
    }

    private fun initRecyclerView() {
        rv_characters_location.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            characterListAdapter = CharacterListAdapter(this@LocationDetailFragment, requestManager)
            adapter = characterListAdapter
        }
    }
}