package es.i12capea.rickandmortyapiclient.presentation.characters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersStateEvent
import es.i12capea.rickandmortyapiclient.presentation.common.displayErrorDialog
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.episodes.EpisodeListAdapter
import es.i12capea.rickandmortyapiclient.presentation.episodes.EpisodeListAdapterDeepLink
import kotlinx.android.synthetic.main.character_detail_scroll_layout.*
import kotlinx.android.synthetic.main.fragment_character_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    @Inject
    lateinit var requestManager : RequestManager

    @Inject
    lateinit var episodeListAdapter : EpisodeListAdapterDeepLink

    private val viewModel : CharactersViewModel by  activityViewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
            .addListener(object : Transition.TransitionListener{
                override fun onTransitionEnd(transition: Transition) {
                    args.character?.let {
                        viewModel.setCharacterDetails(it)
                        viewModel.setStateEvent(CharactersStateEvent.GetEpisodesFromCharacter(it))
                    } ?: kotlin.run {
                        viewModel.setStateEvent(CharactersStateEvent.GetCharacter(args.characterId))
                    }

                    collapsing_toolbar.title = args.characterName
                }
                override fun onTransitionResume(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionStart(transition: Transition) {}
            })
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        initRecyclerView()

        subscribeObservers()

        expandedImage.apply {
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
        rv_episodes.apply {
            layoutManager = LinearLayoutManager(this@CharacterDetailFragment.context)
            adapter = episodeListAdapter
        }
    }

    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { viewState ->
                viewState.episodes?.let {
                    viewModel.setEpisodeList(it)
                }
                viewState.character?.let {
                    viewModel.setCharacterDetails(it)
                }
            }
            dataState.loading.let {

            }
            dataState.error?.let {
                this.displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.episodes?.let {
                episodeListAdapter.submitList(it)
            }
            viewState.character?.let {
                setCharacterView(it)
                viewModel.setStateEvent(CharactersStateEvent.GetEpisodesFromCharacter(it))
            }
        })
    }

    private fun setCharacterView(character: Character) {
        tv_status.text = character.status
        tv_specie.text = character.species
        tv_location_info.text = character.location.name
        collapsing_toolbar.title = character.name
    }


}