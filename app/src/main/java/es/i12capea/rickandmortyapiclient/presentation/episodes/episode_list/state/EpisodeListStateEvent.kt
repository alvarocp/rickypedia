package es.i12capea.rickandmortyapiclient.presentation.episodes.episode_list.state

sealed class EpisodeListStateEvent {
    class GetNextPage() : EpisodeListStateEvent()
}