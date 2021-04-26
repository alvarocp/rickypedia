package es.i12capea.rickypedia.features.locations.location_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.domain.common.Constants
import es.i12capea.rickypedia.common.displayErrorDialog
import es.i12capea.rickypedia.databinding.FragmentLocationDetailBinding
import es.i12capea.rickypedia.entities.Location
import es.i12capea.rickypedia.features.characters.character_list.CharacterListAdapterDeepLink
import es.i12capea.rickypedia.features.locations.location_detail.state.LocationDetailStateEvent.GetCharactersInLocation
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LocationDetailFragment
    : Fragment()
{
    private var _binding: FragmentLocationDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : LocationDetailViewModel by viewModels()

    private val args: LocationDetailFragmentArgs by navArgs()

    lateinit var characterListAdapterDeepLink: CharacterListAdapterDeepLink

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        subscribeObservers()

        initRecyclerView()

        viewModel.getLocation()?.let { location ->
            viewModel.getCharactersInLocation()?.let { characters ->
                setLocation(location)
                characterListAdapterDeepLink.submitList(characters)
            }
        } ?: kotlin.run {
            args.location?.let {
                setLocation(it)
                viewModel.setStateEvent(GetCharactersInLocation(it))
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun subscribeObservers() {
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.viewState.collect { viewState ->
                viewState.characters?.let {
                    characterListAdapterDeepLink.submitList(it)
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                    if(it.isEmpty()){
                        binding.clEmptyLocation.visibility = View.VISIBLE
                    }else{
                        binding.clEmptyLocation.visibility = View.INVISIBLE
                    }
                }
                viewState.errorRym.getContentIfNotHandled()?.let { error ->
                    if(error.code != Constants.NO_ERROR){
                        displayErrorDialog(error.desc)
                    }
                }
                viewState.isLoading.let { isLoading ->
                    if(isLoading){
                        binding.progressBar.visibility = View.VISIBLE
                    }else{
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    fun setLocation(it: Location){
        binding.tvLocationName.text = it.name
        binding.tvLocationDimension.text = it.dimension
        binding.tvLocationType.text = it.type

        viewModel.setLocation(it)
    }

    private fun initRecyclerView() {
        binding.rvCharactersLocation.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
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