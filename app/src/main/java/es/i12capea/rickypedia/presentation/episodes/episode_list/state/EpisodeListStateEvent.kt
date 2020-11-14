package es.i12capea.rickypedia.presentation.episodes.episode_list.state

sealed class EpisodeListStateEvent {
    class GetNextPage() : EpisodeListStateEvent()
}