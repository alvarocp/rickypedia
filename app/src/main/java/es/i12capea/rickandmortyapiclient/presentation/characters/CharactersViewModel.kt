package es.i12capea.rickandmortyapiclient.presentation.characters

import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import es.i12capea.rickandmortyapiclient.common.DataState
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

    @ExperimentalCoroutinesApi
    override fun setStateEvent(stateEvent: CharactersStateEvent)  {
        clearCompletedJobs()

        val jobName = when(stateEvent){
            is CharactersStateEvent.GetNextCharacterPage -> {
                CharactersStateEvent.GetNextCharacterPage::class.java.name + getNextPage()
            }
            is CharactersStateEvent.GetEpisodesFromCharacter -> {
                CharactersStateEvent.GetEpisodesFromCharacter::class.java.name
            }
            is CharactersStateEvent.GetCharacter -> {
                CharactersStateEvent.GetCharacter::class.java.name
            }
            else -> { "" }
        }

        getJob(jobName)?.let {

        } ?: kotlin.run {
            val job = launch {
                dataState.postValue(DataState.loading(true))

                when(stateEvent){

                    is CharactersStateEvent.GetNextCharacterPage -> {
                        getNextPage()?.let {nextPage ->
                            getCharacters.invoke(nextPage)
                                .flowOn(Dispatchers.IO)
                                .onCompletion { cause ->
                                    withContext(Dispatchers.Main) {
                                        handleCompletion(cause)
                                    }
                                }
                                .collect{
                                    handleCollectCharacters(it.characterPageEntityToPresentation())
                                }
                        }
                    }


                    is CharactersStateEvent.GetEpisodesFromCharacter -> {
                        getEpisodes.invoke(stateEvent.character.episodes)
                            .flowOn(Dispatchers.IO)
                            .onCompletion { cause -> withContext(Dispatchers.Main){
                                handleCompletion(cause)
                            } }
                            .collect{
                                handleCollectEpisodes(it.episodeListToPresentation())
                            }
                    }

                    is CharactersStateEvent.GetCharacter -> {
                        getCharacter.invoke(stateEvent.id)
                            .flowOn(Dispatchers.IO)
                            .onCompletion { cause -> withContext(Dispatchers.Main){
                                handleCompletion(cause)
                            } }
                            .collect {
                                handleCollectCharacter(it.toPresentation())
                            }
                    }
                }
            }

            job.invokeOnCompletion {
                removeJobFromList(jobName)
            }
            addJob(jobName, job)
        }



    }

    private fun handleCollectCharacter(character: Character) {
        dataState.postValue(DataState.success(
            CharactersViewState(
                character = character
            )
        ))
    }

    private fun handleCollectEpisodes(episodes: List<Episode>) {
        dataState.postValue(DataState.success(
            CharactersViewState(
                episodes = episodes
            )
        ))
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
        dataState.postValue(DataState.success(
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

    fun getNextPage() : Int? {
        return getCurrentViewStateOrNew().lastPage?.next
    }

    fun getPrevPage() : Int? {
        return getCurrentViewStateOrNew().lastPage?.prev
    }
}