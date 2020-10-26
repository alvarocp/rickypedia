package es.i12capea.rickandmortyapiclient.presentation.episodes.episode_detail.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode

data class EpisodeDetailViewState(
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null,
    var episode: Episode? = null
)