package es.i12capea.rickandmortyapiclient.presentation.characters

import android.os.Parcel
import android.os.Parcelable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import es.i12capea.rickandmortyapiclient.common.DataState
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.domain.exceptions.PredicateNotSatisfiedException
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import es.i12capea.rickandmortyapiclient.domain.exceptions.ResponseException
import es.i12capea.rickandmortyapiclient.domain.usecases.GetAllCharactersUseCase
import es.i12capea.rickandmortyapiclient.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersStateEvent
import es.i12capea.rickandmortyapiclient.presentation.characters.state.CharactersViewState
import es.i12capea.rickandmortyapiclient.presentation.common.BaseViewModel
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.characterListToPresentation
import es.i12capea.rickandmortyapiclient.presentation.entities.mappers.episodeListToPresentation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class CharactersViewModel @ViewModelInject constructor(
    private val getAllCharacters: GetAllCharactersUseCase,
    private val getEpisodes: GetEpisodesUseCase
) : BaseViewModel<CharactersStateEvent, CharactersViewState>(){

    @ExperimentalCoroutinesApi
    override fun setStateEvent(stateEvent: CharactersStateEvent)  {
        var jobName = ""
        val job = launch {
            dataState.postValue(DataState.loading(true))

            when(stateEvent){

                is CharactersStateEvent.GetAllCharacters -> {
                    jobName = CharactersStateEvent.GetAllCharacters::class.java.name
                        getAllCharacters.invoke(stateEvent.page)
                            .flowOn(Dispatchers.IO)
                            .onCompletion { cause -> withContext(Dispatchers.Main) {
                                handleCompletion(cause, stateEvent.page + 1)
                                }
                            }
                            .collect{ handleCollectCharacters(it.characterListToPresentation(), stateEvent.page) }
                    }

                is CharactersStateEvent.GetEpisodesFromCharacter -> {
                    jobName = CharactersStateEvent.GetEpisodesFromCharacter::class.java.name
                    getEpisodes.invoke(stateEvent.character.episode)
                        .flowOn(Dispatchers.IO)
                        .onCompletion { cause -> withContext(Dispatchers.Main){
                            handleCompletion(cause)
                        } }
                        .collect{
                            handleCollectEpisodes(it.episodeListToPresentation())
                        }
                    }
            }
        }
        viewModelScope.launch {
            addJob(jobName, job)
        }
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

    fun getActualPage() : Int{
        return getCurrentViewStateOrNew().page
    }

    fun setActualPage(page: Int){
        val update = getCurrentViewStateOrNew()
        update.page = page
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

    fun handleCompletion(cause: Throwable?, page: Int){
        cause?.let {
            handleError(it)
        }
        setActualPage(page)
        dataState.postValue(DataState.loading(false))
    }

    fun handleCompletion(cause: Throwable?){
        cause?.let {
            handleError(it)
        }
        dataState.postValue(DataState.loading(false))
    }

    fun handleError(cause: Throwable){
        when(cause){
            is RequestException -> {
                dataState.postValue(
                    DataState.error(1, "No se ha podido realizar la conexión.")
                )
            }
            is ResponseException -> {
                dataState.postValue(
                    DataState.error(2, "Error en la respuesta de servidor.")
                )
            }
            is PredicateNotSatisfiedException -> {
                dataState.postValue(
                    DataState.error(3, "No se ha cumplido los predicados.")
                )
            }
            else -> {
                dataState.postValue(
                    DataState.error(9999, "Error desconocido")
                )
            }
        }
    }

    fun handleCollectCharacters(characters: List<Character>, page: Int ){
        dataState.postValue(DataState.success(
            CharactersViewState(
                page = page,
                characters = characters
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
}