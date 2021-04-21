package es.i12capea.rickypedia.features.characters.character_list

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import es.i12capea.domain.usecases.GetCharactersInPageUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Page
import es.i12capea.rickypedia.entities.mappers.characterPageEntityToPresentation
import es.i12capea.rickypedia.features.characters.character_list.state.CharacterListStateEvent
import es.i12capea.rickypedia.features.characters.character_list.state.CharacterListViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CharacterListViewModel @ViewModelInject constructor(
    private val getCharacters: GetCharactersInPageUseCase,
    dispatcher: CoroutineDispatcher
) : BaseViewModel<CharacterListStateEvent, CharacterListViewState>(dispatcher){

    override fun getJobNameForEvent(stateEvent: CharacterListStateEvent) : String {
        return when(stateEvent){
            is CharacterListStateEvent.GetNextCharacterPage -> {
                CharacterListStateEvent.GetNextCharacterPage::class.java.name + getNextPage()
            }
        }
    }

    override fun getJobForEvent(stateEvent: CharacterListStateEvent): Job {
        return viewModelScope.launch {
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

    private suspend fun getNextCharacterPageFlow(nextPage: Int){
        val currentCharacters = getCharacterList()
        try {
            getCharacters.invoke(nextPage)
                .flowOn(Dispatchers.IO)
                .collect {
                    handleCollectCharacters(currentCharacters, it.characterPageEntityToPresentation())
                }
        } catch (t: Throwable){
            handleThrowable(t)
        }
    }

    fun getActualPage() : Page<Character>?{
        return getCurrentViewState().lastPage
    }

    private suspend fun setActualPage(page: Page<Character>){
        val update = getCurrentViewState()
        setViewState(update.copy(lastPage = page))
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewState().characters
    }

    private suspend fun setCharacterList(list: List<Character>){
        val update = getCurrentViewState()
        setViewState(update.copy(characters = list))
    }

    private suspend fun handleCollectCharacters(currentList: List<Character>?, page: Page<Character>){
        setActualPage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

       setCharacterList(list)
    }

    override fun initNewViewState(): CharacterListViewState {
        return CharacterListViewState(
            lastPage = Page(
                next = 1,
                prev = null,
                actual = 0,
                list = emptyList(),
                count = 0
            ),
            characters = null
        )
    }

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(layoutManagerState = state)) }
    }

    fun getRecyclerState() : Parcelable? {
        return getCurrentViewState().layoutManagerState
    }

    private fun getLastPage() : Page<Character>?{
        return getCurrentViewState().lastPage
    }

    private fun getNextPage() : Int? {
        return getCurrentViewState().lastPage?.next
    }

    override fun setLoading(isLoading: Boolean): CharacterListViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): CharacterListViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }
}