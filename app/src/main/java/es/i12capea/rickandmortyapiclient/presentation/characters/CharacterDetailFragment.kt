package es.i12capea.rickandmortyapiclient.presentation.characters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.transition.addListener
import androidx.core.transition.doOnEnd
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.character_detail_scroll_layout.*
import kotlinx.android.synthetic.main.fragment_character_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {


    @Inject lateinit var requestManager : RequestManager

    private lateinit var episodeListAdapter : EpisodeListAdapter2

    private val viewModel : CharactersViewModel by  viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.character?.let { character ->
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.move)
                .addListener(object : Transition.TransitionListener{
                    override fun onTransitionEnd(transition: Transition) {
                        viewModel.setStateEvent(CharactersStateEvent.GetEpisodesFromCharacter(character))
                        collapsing_toolbar.title = character.name

                    }
                    override fun onTransitionResume(transition: Transition) {}
                    override fun onTransitionPause(transition: Transition) {}
                    override fun onTransitionCancel(transition: Transition) {}
                    override fun onTransitionStart(transition: Transition) {}
                })
            sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

            postponeEnterTransition()
        }

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        initRecyclerView()

        subscribeObservers()


        args.character?.let { character ->

            expandedImage.apply {
                transitionName = character.id.toString()
                Glide.with(this)
                    .load(character.image)
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

            tv_status.text = character.status
            tv_specie.text = character.species
            tv_location_info.text = character.location.name

        }

    }

    private fun initRecyclerView(){
        rv_episodes.apply {
            layoutManager = LinearLayoutManager(this@CharacterDetailFragment.context)
            episodeListAdapter = EpisodeListAdapter2()
            adapter = episodeListAdapter
        }
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let {viewState ->
                viewState.episodes?.let {
                    viewModel.setEpisodeList(it)
                }
            }
            dataState.loading.let {

            }
            dataState.error?.let {
                this.displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.episodes?.let {
                episodeListAdapter.submitList(it)
            }
        })
    }

}