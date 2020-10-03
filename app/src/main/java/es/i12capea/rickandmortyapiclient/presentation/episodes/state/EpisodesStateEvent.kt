package es.i12capea.rickandmortyapiclient.presentation.episodes.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode

sealed class EpisodesStateEvent {
    class GetEpisode(val episodeId: Int) : EpisodesStateEvent()
    class GetAllEpisodes(val page: Int = 1) : EpisodesStateEvent()
    class GetEpisodesFromCharacter(val character: Character) : EpisodesStateEvent()
    class GetCharactersInEpisode(val episode: Episode) : EpisodesStateEvent()
}