package es.i12capea.rickandmortyapiclient.presentation.characters.character_list

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInPageUseCase
import es.i12capea.rickandmortyapiclient.presentation.characters.character_list.state.CharacterListStateEvent
import es.i12capea.rickandmortyapiclient.presentation.characters.character_list.state.CharacterListViewState
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterPageEntityToPresentation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class CharacterListViewModel @ViewModelInject constructor(
    private val getCharacters: GetCharactersInPageUseCase,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel<CharacterListStateEvent, CharacterListViewState>(dispatcher){

    init {
        val update = getCurrentViewStateOrNew()
        update.lastPage = Page(
            next = 1,
            prev = null,
            actual = 0,
            list = emptyList(),
            count = 0
        )
        update.characters = null
        setViewState(update)
    }

    override fun getJobNameForEvent(stateEvent: CharacterListStateEvent) : String?{
        return when(stateEvent){
            is CharacterListStateEvent.GetNextCharacterPage -> {
                CharacterListStateEvent.GetNextCharacterPage::class.java.name + getNextPage()
            }
        }
    }

    private suspend fun getNextCharacterPageFlow(nextPage: Int){
        val currentCharacters = getCharacterList()
        try {
            getCharacters.invoke(nextPage)
                .flowOn(Dispatchers.IO)
                .onCompletion { cause ->
                    handleCompletion(cause)
                }
                .collect {
                    handleCollectCharacters(currentCharacters, it.characterPageEntityToPresentation())
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }

    }

    override fun getJobForEvent(stateEvent: CharacterListStateEvent): Job? {
        return launch {
            when (stateEvent) {
                is CharacterListStateEvent.GetNextCharacterPage -> {
                    getLastPage()?.let { currentPage ->
                        currentPage.next?.let { nextPage ->
                            getNextCharacterPageFlow(nextPage)
                        }
                    }
                }
            }
        }
    }


    fun getActualPage() : Page<Character>?{
        return getCurrentViewStateOrNew().lastPage
    }

    fun setActualPage(page: Page<Character>){
        val update = getCurrentViewStateOrNew()
        update.lastPage = page
        postViewState(update)
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewStateOrNew().characters
    }

    fun setCharacterList(list: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = list
        postViewState(update)
    }

    fun handleCollectCharacters(currentList: List<Character>?, page: Page<Character> ){
        setActualPage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        setCharacterList(list)
    }

    private fun postLastPage(lastPage: Page<Character>) {
        dataState.postValue(Event(
            CharacterListViewState(
                lastPage = lastPage
            )
        ))
    }


    override fun initNewViewState(): CharacterListViewState {
        return CharacterListViewState()
    }

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = state
        postViewState(update)
    }

    fun getRecyclerState() : Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }

    fun getLastPage() : Page<Character>?{
        return getCurrentViewStateOrNew().lastPage
    }

    fun getNextPage() : Int? {
        return getCurrentViewStateOrNew().lastPage?.next
    }

    fun getPrevPage() : Int? {
        return getCurrentViewStateOrNew().lastPage?.prev
    }
}