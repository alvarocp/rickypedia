package es.i12capea.rickypedia.presentation.episodes.episode_detail.state

import es.i12capea.rickypedia.presentation.entities.Episode

sealed class EpisodeDetailStateEvent {
    class GetEpisode(val episodeId: Int) : EpisodeDetailStateEvent()
    class GetCharactersInEpisode(val episode: Episode) : EpisodeDetailStateEvent()
}