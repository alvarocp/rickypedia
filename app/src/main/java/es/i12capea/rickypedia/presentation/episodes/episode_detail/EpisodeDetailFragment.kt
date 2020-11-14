package es.i12capea.rickypedia.presentation.episodes.episode_detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.R
import es.i12capea.rickypedia.presentation.characters.character_list.CharacterListAdapterDeepLink
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.episodes.episode_detail.state.EpisodeDetailStateEvent
import kotlinx.android.synthetic.main.episode_item.view.*
import kotlinx.android.synthetic.main.fragment_episode_detail.*
import kotlinx.android.synthetic.main.fragment_episode_detail.iv_back
import kotlinx.android.synthetic.main.fragment_episode_detail.progress_bar
import kotlinx.android.synthetic.main.fragment_location_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class EpisodeDetailFragment
    : Fragment(R.layout.fragment_episode_detail)
{

    private val viewModel : EpisodeDetailViewModel by viewModels()

    private val args: EpisodeDetailFragmentArgs by navArgs()

    lateinit var characterListAdapterDeepLink: CharacterListAdapterDeepLink

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

        iv_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.getContentIfNotHandled()?.let {viewState ->
                viewState.characters?.let {
                    rv_characters_episode.visibility = View.VISIBLE
                    viewModel.setCharacterList(it)
                }
                viewState.episode?.let {
                    onEpisodeChange(it)
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                displayErrorDialog(it.desc)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if(it){
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.INVISIBLE
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.characters?.let {
                characterListAdapterDeepLink.submitList(it)
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    fun onEpisodeChange(it: Episode){
        setEpisodeView(it)
        viewModel.setCurrentEpisode(it)
        viewModel.setStateEvent(EpisodeDetailStateEvent.GetCharactersInEpisode(it))
    }

    fun setEpisodeView(it: Episode){
        layout_episode.tv_title.text = it.name
        layout_episode.tv_episode.text = it.episode
        tv_air_date.text = it.air_date
    }

    private fun initRecyclerView(){
        rv_characters_episode.apply {
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