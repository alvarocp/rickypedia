package es.i12capea.rickypedia.features.episodes.episode_detail.state

import es.i12capea.rickypedia.entities.Episode

sealed class EpisodeDetailStateEvent {
    class GetEpisodeAndCharactersInEpisode(val episodeId: Int) : EpisodeDetailStateEvent()
    class GetCharactersInEpisode(val episode: Episode) : EpisodeDetailStateEvent()
}