package es.i12capea.rickandmortyapiclient.presentation.characters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersStateEvent
import es.i12capea.rickandmortyapiclient.presentation.common.displayErrorDialog
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import kotlinx.android.synthetic.main.character_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class CharacterListFragment (
) : Fragment(),
    CharacterListAdapter.Interaction
{

    val TAG = "Pruebas"

    @Inject
    lateinit var requestManager: RequestManager


    private val viewModel : CharactersViewModel by  activityViewModels()

    override fun onItemSelected(position: Int, item: Character, imageView: ImageView) {

        val extras = FragmentNavigatorExtras(
            imageView to imageView.transitionName
        )
        val direction = CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
            character = item
        )
        findNavController().navigate(direction, extras)
    }

    lateinit var characterListAdapter : CharacterListAdapter

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        subscribeObservers()

        rv_characters.adapter?.let {
            handleCharacters()
        }?: kotlin.run {
            initRecyclerView()
            handleCharacters()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.character_list_fragment, container, false)
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.lastPage?.let {
                    viewModel.setActualPage(it)
                    viewModel.addToCharacterList(it.list)
                }
            }

            dataState.loading.let {
                if(it){
                    progress_bar.visibility = View.VISIBLE
                }else{
                    progress_bar.visibility = View.INVISIBLE
                }
            }

            dataState.error?.let {
                this.displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.characters?.let {
                characterListAdapter.submitList(it)
            }

            viewState.character?.let {

            }
        })
    }

    private fun initRecyclerView(){
        rv_characters.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            characterListAdapter = CharacterListAdapter(this@CharacterListFragment, requestManager)
            adapter = characterListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                    val lastPosition = IntArray(layoutManager.spanCount)
                    layoutManager.findLastVisibleItemPositions(lastPosition)
                    if (lastPosition[0] >= characterListAdapter.itemCount.minus(4)) {
                        viewModel.setStateEvent(CharactersStateEvent.GetNextCharacterPage())
                        Log.d("A", "LastPositionReached")
                    }
                }
            })

            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun handleCharacters(){
        viewModel.getCharacterList()?.let { characters ->
            viewModel.getRecyclerState()?.let { parcel ->
                characterListAdapter.submitList(characters)
                rv_characters.layoutManager?.onRestoreInstanceState(parcel)
                viewModel.setRecyclerState(null)
            }
        } ?: kotlin.run {
            viewModel.setStateEvent(CharactersStateEvent.GetNextCharacterPage())
            Log.d(TAG, "onViewCreated without characters")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroy")
        viewModel.setRecyclerState(rv_characters.layoutManager?.onSaveInstanceState())
    }
}