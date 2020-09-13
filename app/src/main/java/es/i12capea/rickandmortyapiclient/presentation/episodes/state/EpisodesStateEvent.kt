package es.i12capea.rickandmortyapiclient.presentation.episodes.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Character

sealed class EpisodesStateEvent {
    class GetEpisodes(val id: Int) : EpisodesStateEvent()
    class GetAllEpisodes(val page: Int = 1) : EpisodesStateEvent()
    class GetEpisodesFromCharacter(val character: Character) : EpisodesStateEvent()
}