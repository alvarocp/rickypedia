package es.i12capea.rickypedia.presentation.characters.character_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.databinding.FragmentCharacterDetailBinding
import es.i12capea.rickypedia.presentation.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.common.visible
import es.i12capea.rickypedia.presentation.entities.Character
import es.i12capea.rickypedia.presentation.episodes.episode_list.EpisodeListAdapterDeepLink
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CharacterDetailFragment
    : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : CharacterDetailViewModel by viewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    lateinit var episodeListAdapter: EpisodeListAdapterDeepLink


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
            .addListener(object : Transition.TransitionListener{
                override fun onTransitionEnd(transition: Transition) {
                    viewModel.setImageLoad(true)
                }
                override fun onTransitionResume(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionStart(transition: Transition) {}
            })
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
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

        adjustInset()


        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        initRecyclerView()

        subscribeObservers()

        binding.expandedImage.apply {
            transitionName = args.characterId.toString()
            Glide.with(this)
                .load(args.characterImage.replace("\\", "/"))
                .dontAnimate()
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(this)
        }

    }

    private fun initRecyclerView(){
        binding.scrollLayout.rvEpisodes.apply {
            layoutManager = LinearLayoutManager(this@CharacterDetailFragment.context)
            episodeListAdapter = EpisodeListAdapterDeepLink()
            episodeListAdapter.setHasStableIds(true)
            adapter = episodeListAdapter
        }
    }

    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            dataState.getContentIfNotHandled()?.let { viewState ->
                viewState.episodes?.let {
                    viewModel.setEpisodeList(it)
                }
                viewState.character?.let {
                    viewModel.setCharacterDetails(it)
                    viewModel.setStateEvent(CharacterDetailStateEvent.GetEpisodesFromCharacter(it))
                }
                viewState.isImageLoaded?.let { isImageLoaded ->
                    isImageLoaded.let {bool ->
                        if(bool){
                            args.character?.let {
                                viewModel.setCharacterDetails(it)
                                viewModel.setStateEvent(CharacterDetailStateEvent.GetEpisodesFromCharacter(it))
                            } ?: kotlin.run {
                                viewModel.setStateEvent(CharacterDetailStateEvent.GetCharacter(args.characterId))
                            }
                        }
                    }
                    }
                }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect{
                binding.scrollLayout.progressBar.visible = it
            }

            viewModel.viewState.collect {  viewState ->
                viewState.character?.let {
                    setCharacterView(it)
                }
                viewState.episodes?.let {
                    episodeListAdapter.submitList(it)
                }
            }

            viewModel.error.collect {
                displayErrorDialog(it.desc)
            }
        }

    }

    private fun setCharacterView(character: Character) {
        binding.scrollLayout.tvStatus.text = character.status
        binding.scrollLayout.tvSpecie.text = character.species
        binding.scrollLayout.tvLocationInfo.text = character.location.name
        binding.collapsingToolbar.title = character.name
    }

    private fun adjustInset(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { _, insets ->
            (binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams).topMargin = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }
    }

}