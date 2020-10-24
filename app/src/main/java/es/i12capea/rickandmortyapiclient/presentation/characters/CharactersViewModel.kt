package es.i12capea.rickandmortyapiclient.presentation.characters

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharacterUseCase
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.domain.usecases.GetCharactersInPage
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersStateEvent
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersViewState
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.Page
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterPageEntityToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.episodeListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.toPresentation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class CharactersViewModel @ViewModelInject constructor(
    private val getCharacters: GetCharactersInPage,
    private val getEpisodes: GetEpisodesUseCase,
    private val getCharacter: GetCharacterUseCase
) : BaseViewModel<CharactersStateEvent, CharactersViewState>(){

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

    override fun getJobNameForEvent(stateEvent: CharactersStateEvent) : String?{
        return when(stateEvent){
            is CharactersStateEvent.GetNextCharacterPage -> {
                CharactersStateEvent.GetNextCharacterPage::class.java.name + getNextPage()
            }
            is CharactersStateEvent.GetEpisodesFromCharacter -> {
                CharactersStateEvent.GetEpisodesFromCharacter::class.java.name
            }
            is CharactersStateEvent.GetCharacter -> {
                CharactersStateEvent.GetCharacter::class.java.name
            }
            else -> { null }
        }
    }

    private suspend fun getNextCharacterFlow(nextPage: Int){
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

    override fun getJobForEvent(stateEvent: CharactersStateEvent): Job? {
        return launch {
            when (stateEvent) {
                is CharactersStateEvent.GetNextCharacterPage -> {
                    getLastPage()?.let { currentPage ->
                        currentPage.next?.let { nextPage ->
                            getNextCharacterFlow(nextPage)
                        }
                    }
                }

                is CharactersStateEvent.GetEpisodesFromCharacter -> {
                    getEpisodes(stateEvent.character.episodes)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                            handleCompletion(cause)
                        }
                        .collect {
                            handleCollectEpisodes(it.episodeListToPresentation())
                        }
                }

                is CharactersStateEvent.GetCharacter -> {
                    getCharacter(stateEvent.id)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause ->
                            handleCompletion(cause)
                        }
                        .collect {
                            handleCollectCharacter(it.toPresentation())
                        }
                }
            }
        }
    }

    private fun handleCollectCharacter(character: Character) {
        dataState.postValue(Event(
            CharactersViewState(
                character = character
            )
        ))
    }

    fun setImageLoad(isLoad: Boolean){
        dataState.postValue(Event(
            CharactersViewState(
                isImageLoaded = isLoad
            )
        ))
    }

    private fun handleCollectEpisodes(episodes: List<Episode>) {
        dataState.postValue(
            Event(
            CharactersViewState(
                episodes = episodes
            )
        )
        )
    }

    fun getEpisodeList() : List<Episode>?{
        return getCurrentViewStateOrNew().episodes
    }

    fun setEpisodeList(episodes: List<Episode>){
        val update = getCurrentViewStateOrNew()
        update.episodes = episodes
        postViewState(update)
    }

    fun setCharacterDetails(character: Character){
        val update = getCurrentViewStateOrNew()
        update.character = character
        postViewState(update)
    }

    fun getCharacterDetails() : Character?{
        return getCurrentViewStateOrNew().character
    }

    fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = cl
        postViewState(update)
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

    fun addToCharacterList(list: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = list
        postViewState(update)
    }

    fun handleCollectCharacters(currentList: List<Character>?, page: Page<Character> ){
        setActualPage(page)

        val list = currentList?.toMutableList()
            ?: arrayListOf()
        list.addAll(page.list)

        addToCharacterList(list)
    }

    private fun postLastPage(lastPage: Page<Character>) {
        dataState.postValue(Event(
            CharactersViewState(
                lastPage = lastPage
            )
        ))
    }


    override fun initNewViewState(): CharactersViewState {
        return CharactersViewState()
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