package es.i12capea.rickypedia.presentation.characters.character_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.i12capea.rickypedia.databinding.FragmentCharacterListBinding
import es.i12capea.rickypedia.presentation.characters.character_list.state.CharacterListStateEvent
import es.i12capea.rickypedia.presentation.common.displayErrorDialog
import es.i12capea.rickypedia.presentation.common.visible
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CharacterListFragment constructor(
) : Fragment()
{

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CharacterListViewModel by activityViewModels()

    lateinit var characterListAdapter : CharacterListAdapter

    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRecyclerState(binding.rvCharacters.layoutManager?.onSaveInstanceState())
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        initRecyclerView()

        handleCharacters()

    }

    private fun subscribeObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { viewState ->
                viewState.characters?.let {
                    characterListAdapter.submitList(it)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect{
                binding.progressBar.visible = it
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collect{
                Log.d("stateerror", "recogido")
                displayErrorDialog(it.desc)
            }
        }

    }

    private fun initRecyclerView(){
        binding.rvCharacters.apply {
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
                    binding.rvCharacters.layoutManager?.onRestoreInstanceState(parcel)
                    viewModel.setRecyclerState(null)
                }
            }

        } ?: kotlin.run {
            viewModel.setStateEvent(CharacterListStateEvent.GetNextCharacterPage())
        }
    }
}