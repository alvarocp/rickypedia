package es.i12capea.rickypedia.features.characters.character_detail

import dagger.hilt.android.lifecycle.HiltViewModel
import es.i12capea.rickypedia.shared.domain.usecases.GetCharacterUseCase
import es.i12capea.rickypedia.shared.domain.usecases.GetEpisodesUseCase
import es.i12capea.rickypedia.common.BaseViewModel
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.mappers.episodeListToPresentation
import es.i12capea.rickypedia.entities.mappers.toPresentation
import es.i12capea.rickypedia.features.characters.character_detail.state.CharacterDetailStateEvent
import es.i12capea.rickypedia.features.characters.character_detail.state.CharacterDetailViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacter: GetCharacterUseCase,
    private val getEpisodes: GetEpisodesUseCase,
    dispatcher: CoroutineDispatcher
    ) : BaseViewModel<CharacterDetailStateEvent, CharacterDetailViewState>(dispatcher){


    override fun getJobNameForEvent(stateEvent: CharacterDetailStateEvent): String {
        return when(stateEvent){
            is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                CharacterDetailStateEvent.GetEpisodesFromCharacter::class.java.name
            }
            is CharacterDetailStateEvent.GetCharacter -> {
                CharacterDetailStateEvent.GetCharacter::class.java.name
            }
            is CharacterDetailStateEvent.GetCharacterAndEpisodes -> {
                CharacterDetailStateEvent.GetCharacterAndEpisodes::class.java.name
            }
        }
    }

    override fun getJobForEvent(stateEvent: CharacterDetailStateEvent): Job {
        return launch {
            when (stateEvent) {
                is CharacterDetailStateEvent.GetEpisodesFromCharacter -> {
                    try {
                        getEpisodes(stateEvent.character.episodes)
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectEpisodes(it.episodeListToPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
                    }
                }

                is CharacterDetailStateEvent.GetCharacter -> {
                    try {
                        getCharacter(stateEvent.id)
                            .flowOn(Dispatchers.IO)
                            .collect {
                                handleCollectCharacter(it.toPresentation())
                            }
                    }catch (t: Throwable){
                        handleThrowable(t)
                    }
                }
                is CharacterDetailStateEvent.GetCharacterAndEpisodes -> {
                    try {
                        getCharacter(stateEvent.id)
                                .flowOn(Dispatchers.IO)
                                .collect { character ->
                                    handleCollectCharacter(character.toPresentation())
                                    getEpisodes(character.episodes)
                                            .collect {
                                                handleCollectEpisodes(it.episodeListToPresentation())
                                            }
                                }
                    } catch (t: Throwable){
                        handleThrowable(t)
                    }
                }
            }
        }
    }


    override fun initNewViewState(): CharacterDetailViewState {
        return CharacterDetailViewState()
    }

    private suspend fun handleCollectCharacter(character: Character) {
        setCharacterDetails(character)
    }

    fun setImageLoad(isLoad: Boolean){
        val update = getCurrentViewState()
        launch { setViewState(update.copy(isImageLoaded = isLoad)) }
    }

    private suspend fun handleCollectEpisodes(episodes: List<Episode>) {
        setEpisodeList(episodes)
    }

    fun getEpisodeList() : List<Episode>?{
        return getCurrentViewState().episodes
    }

    private suspend fun setEpisodeList(episodes: List<Episode>){
        val update = getCurrentViewState()
        setViewState(update.copy(episodes = episodes))
    }

    private suspend fun setCharacterDetails(character: Character){
        val update = getCurrentViewState()
        setViewState(update.copy(character = character))
    }

    fun getCharacterDetails() : Character?{
        return getCurrentViewState().character
    }

    override fun setLoading(isLoading: Boolean): CharacterDetailViewState {
        val update = getCurrentViewState()
        return update.copy(isLoading = isLoading)
    }

    override fun setError(error: Event<ErrorRym>): CharacterDetailViewState {
        val update = getCurrentViewState()
        return update.copy(errorRym = error)
    }
}
