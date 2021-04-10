package es.i12capea.rickypedia.features.episodes.episode_list.state

sealed class EpisodeListStateEvent {
    object GetNextPage : EpisodeListStateEvent()
}