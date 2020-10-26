package es.i12capea.rickandmortyapiclient.presentation.characters.character_list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickandmortyapiclient.R
import es.i12capea.rickandmortyapiclient.presentation.characters.character_list.state.CharacterListStateEvent
import es.i12capea.rickandmortyapiclient.presentation.common.displayErrorDialog
import kotlinx.android.synthetic.main.character_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CharacterListFragment constructor(
) : Fragment(R.layout.character_list_fragment)
{
    val TAG = "Pruebas"

    private val viewModel : CharacterListViewModel by activityViewModels()

    lateinit var characterListAdapter : CharacterListAdapter

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        subscribeObservers()

        initRecyclerView()

        handleCharacters()

        super.onViewCreated(view, savedInstanceState)

    }

    fun subscribeObservers() {

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                progress_bar.visibility = View.VISIBLE
            }else{
                progress_bar.visibility = View.INVISIBLE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                displayErrorDialog(it.desc)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.characters?.let {
                characterListAdapter.submitList(it)
            }
        })
    }

    private fun initRecyclerView(){
        rv_characters.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            characterListAdapter = CharacterListAdapter()
            characterListAdapter.setHasStableIds(true)
            adapter = characterListAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager =
                        recyclerView.layoutManager as StaggeredGridLayoutManager
                    val lastPosition = IntArray(layoutManager.spanCount)
                    layoutManager.findLastVisibleItemPositions(lastPosition)
                    if (lastPosition[0] >= characterListAdapter.itemCount.minus(4)) {
                        viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacterPage())
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
            viewModel.getActualPage()?.let { actualPage ->
                viewModel.getRecyclerState()?.let { parcel ->
                    characterListAdapter.submitList(characters)
                    rv_characters.layoutManager?.onRestoreInstanceState(parcel)
                    viewModel.setRecyclerState(null)
                }
            }

        } ?: kotlin.run {
            viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacterPage())
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