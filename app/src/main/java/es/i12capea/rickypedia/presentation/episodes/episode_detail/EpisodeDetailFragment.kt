package es.i12capea.rickypedia.presentation.episodes.episode_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.databinding.FragmentEpisodeDetailBinding
import es.i12capea.rickypedia.presentation.characters.character_list.CharacterListAdapterDeepLink
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.common.visible
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.episodes.episode_detail.state.EpisodeDetailStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EpisodeDetailFragment
    : Fragment()
{
    private var _binding: FragmentEpisodeDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : EpisodeDetailViewModel by viewModels()

    private val args: EpisodeDetailFragmentArgs by navArgs()

    lateinit var characterListAdapterDeepLink: CharacterListAdapterDeepLink

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        //vengo del back?
        viewModel.getCurrentEpisode()?.let { episode ->
            viewModel.getCharacterList()?.let { characters ->
                setEpisodeView(episode)
                characterListAdapterDeepLink.submitList(characters)
            }
        } ?: kotlin.run {
            //Vengo de nuevas
            args.episode?.let {
                onEpisodeChange(it)
            } ?: kotlin.run {
                if(args.episodeId > 0){
                    viewModel.setStateEvent(EpisodeDetailStateEvent.GetEpisode(args.episodeId))
                }
            }
        }

        initRecyclerView()

        subscribeObservers()

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.getContentIfNotHandled()?.let {viewState ->
                viewState.characters?.let {
                    binding.rvCharactersEpisode.visibility = View.VISIBLE
                    viewModel.setCharacterList(it)
                }
                viewState.episode?.let {
                    onEpisodeChange(it)
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect{
                binding.progressBar.visible = it
            }

            viewModel.viewState.collect { viewState ->
                viewState.characters?.let {
                    characterListAdapterDeepLink.submitList(it)
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
            }

            viewModel.error.collect {
                displayErrorDialog(it.desc)
            }
        }

    }

    @ExperimentalCoroutinesApi
    fun onEpisodeChange(it: Episode){
        setEpisodeView(it)
        viewModel.setCurrentEpisode(it)
        viewModel.setStateEvent(EpisodeDetailStateEvent.GetCharactersInEpisode(it))
    }

    fun setEpisodeView(it: Episode){
        binding.tvTitle.text = it.name
        binding.tvEpisode.text = it.episode
        binding.tvAirDate.text = it.air_date
    }

    private fun initRecyclerView(){
        binding.rvCharactersEpisode.apply {
            layoutManager = GridLayoutManager(this@EpisodeDetailFragment.context, 2)
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