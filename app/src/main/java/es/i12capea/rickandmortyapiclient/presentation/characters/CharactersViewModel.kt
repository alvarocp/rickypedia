package es.i12capea.rickandmortyapiclient.presentation.characters

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.i12capea.rickandmortyapiclient.common.DataState
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
import java.lang.Exception

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
            list = emptyList()
        )
        update.characters = emptyList()
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
        try {
            getCharacters.invoke(nextPage)
                .flowOn(Dispatchers.IO)
                .onCompletion { cause ->
                    handleCompletion(cause)
                }
                .collect {
                    handleCollectCharacters(it.characterPageEntityToPresentation())
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
                    } ?: kotlin.run {
                        setEpisodeList(emptyList())
                        getNextCharacterFlow(1)
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
        setViewState(update)
    }

    fun setCharacterDetails(character: Character){
        val update = getCurrentViewStateOrNew()
        update.character = character
        setViewState(update)
    }

    fun getCharacterDetails() : Character?{
        return getCurrentViewStateOrNew().character
    }

    fun setCharacterList(cl: List<Character>){
        val update = getCurrentViewStateOrNew()
        update.characters = cl
        setViewState(update)
    }

    fun getActualPage() : Page<Character>?{
        return getCurrentViewStateOrNew().lastPage
    }

    fun setActualPage(page: Page<Character>){
        val update = getCurrentViewStateOrNew()
        update.lastPage = page
        setViewState(update)
    }

    fun getCharacterList() : List<Character>?{
        return getCurrentViewStateOrNew().characters
    }

    fun addToCharacterList(cl: List<Character>){
        val list = getCharacterList()?.toMutableList()
            ?: arrayListOf()
        list.addAll(cl)
        val update = getCurrentViewStateOrNew()
        update.characters = list
        setViewState(update)
    }

    fun handleCollectCharacters(page: Page<Character> ){
        dataState.postValue(Event(
            CharactersViewState(
                lastPage = page
            )
        ))
    }

    override fun initNewViewState(): CharactersViewState {
        return CharactersViewState()
    }

    fun setRecyclerState(state: Parcelable?){
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = state
        setViewState(update)
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