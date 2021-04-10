package es.i12capea.rickypedia.features.episodes.episode_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.domain.common.Constants
import es.i12capea.rickypedia.common.displayErrorDialog
import es.i12capea.rickypedia.databinding.FragmentEpisodeDetailBinding
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.features.characters.character_list.CharacterListAdapterDeepLink
import es.i12capea.rickypedia.features.episodes.episode_detail.state.EpisodeDetailStateEvent
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EpisodeDetailFragment
    : Fragment()
{
    private var _binding: FragmentEpisodeDetailBinding? = null
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
                    viewModel.setStateEvent(EpisodeDetailStateEvent.GetEpisodeAndCharactersInEpisode(args.episodeId))
                }
            }
        }

        initRecyclerView()

        subscribeObservers()

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun subscribeObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { viewState ->
                viewState.characters?.let {
                    binding.rvCharactersEpisode.visibility = View.VISIBLE
                    characterListAdapterDeepLink.submitList(it)
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                } ?: kotlin.run {
                    binding.rvCharactersEpisode.visibility = View.GONE
                }

                viewState.isLoading.let { isLoading ->
                    if(isLoading){
                        binding.progressBar.visibility = View.VISIBLE
                    }else{
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }

                viewState.errorRym.getContentIfNotHandled()?.let { error ->
                    if(error.code != Constants.NO_ERROR){
                        displayErrorDialog(error.desc)
                    }
                }

                viewState.episode?.let {
                    setEpisodeView(it)
                }
            }
        }
    }

    fun onEpisodeChange(it: Episode){
        viewModel.setCurrentEpisode(it)
        viewModel.setStateEvent(EpisodeDetailStateEvent.GetCharactersInEpisode(it))
    }

    private fun setEpisodeView(it: Episode){
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