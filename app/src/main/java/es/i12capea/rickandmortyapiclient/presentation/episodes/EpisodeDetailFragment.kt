package es.i12capea.rickandmortyapiclient.presentation.episodes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import es.i12capea.rickandmortyapiclient.presentation.characters.CharacterListAdapter
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersStateEvent
import es.i12capea.rickandmortyapiclient.presentation.common.displayErrorDialog
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import kotlinx.android.synthetic.main.character_detail_scroll_layout.*
import kotlinx.android.synthetic.main.fragment_character_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeDetailFragment : Fragment(), CharacterListAdapter.Interaction
{

    @Inject lateinit var requestManager : RequestManager

    private lateinit var characterListAdapter : CharacterListAdapter

    private val viewModel : EpisodesViewModel by  viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //private val args: CharacterDetailFragmentArgs by navArgs()

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

        initRecyclerView()

        subscribeObservers()

    }

    private fun initRecyclerView(){
        rv_episodes.apply {
            layoutManager = LinearLayoutManager(this@EpisodeDetailFragment.context)
            characterListAdapter = CharacterListAdapter(this@EpisodeDetailFragment, requestManager)
            adapter = characterListAdapter
        }
    }


    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let {viewState ->
                viewState.characters?.let {
                    viewModel.setCharacterList(it)
                }
            }
            dataState.loading.let {

            }
            dataState.error?.let {
                this.displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.characters?.let {
                characterListAdapter.submitList(it)
            }
        })
    }

    override fun onItemSelected(position: Int, item: Character, imageView: ImageView) {

    }

}