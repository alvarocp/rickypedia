package es.i12capea.rickandmortyapiclient.presentation.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.CharacterListAdapterDeepLink
import es.i12capea.rickandmortyapiclient.presentation.common.displayErrorDialog
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.episodes.state.EpisodesStateEvent
import kotlinx.android.synthetic.main.episode_item.view.*
import kotlinx.android.synthetic.main.fragment_episode_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailFragment : Fragment()
{

    @Inject
    lateinit var requestManager : RequestManager

    @Inject
    lateinit var characterListAdapterDeepLink : CharacterListAdapterDeepLink

    var comesFromBack = false

    private val viewModel : EpisodesViewModel by viewModels()

    private val args: EpisodeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode_detail, container, false)
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
                comesFromBack = true
            }
        } ?: kotlin.run {
            //Vengo de nuevas
            comesFromBack = false

            args.episode?.let {
                onEpisodeChange(it)
            } ?: kotlin.run {
                if(args.episodeId > 0){
                    viewModel.setStateEvent(EpisodesStateEvent.GetEpisode(args.episodeId))
                }
            }
        }

        initRecyclerView()

        subscribeObservers()
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
                if (!comesFromBack){
                    (view?.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    fun onEpisodeChange(it: Episode){
        setEpisodeView(it)
        viewModel.setCurrentEpisode(it)
        viewModel.setStateEvent(EpisodesStateEvent.GetCharactersInEpisode(it))
    }

    fun setEpisodeView(it: Episode){
        layout_episode.tv_title.text = it.name
        layout_episode.tv_episode.text = it.episode
        tv_air_date.text = it.air_date
    }

    private fun initRecyclerView(){
        rv_characters_episode.apply {
            layoutManager = GridLayoutManager(this@EpisodeDetailFragment.context, 2)
            adapter = characterListAdapterDeepLink

            if(comesFromBack){
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
        }
    }
}