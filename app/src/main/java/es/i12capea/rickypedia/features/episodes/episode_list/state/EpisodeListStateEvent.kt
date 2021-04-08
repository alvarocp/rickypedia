package es.i12capea.rickypedia.features.episodes.episode_list.state

sealed class EpisodeListStateEvent {
    class GetNextPage() : EpisodeListStateEvent()
}