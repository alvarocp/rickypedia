package es.i12capea.rickypedia.presentation.episodes.episode_detail.state

import android.os.Parcelable
import es.i12capea.rickypedia.presentation.entities.Character
import es.i12capea.rickypedia.presentation.entities.Episode

data class EpisodeDetailViewState(
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null,
    var episode: Episode? = null
)