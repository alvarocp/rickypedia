package es.i12capea.rickypedia.presentation.episodes.episode_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.presentation.entities.Episode
import es.i12capea.rickypedia.presentation.entities.Page

data class EpisodeListViewState(
    var lastPage: Page<Episode>? = null,
    var layoutManagerState: Parcelable? = null,
    var episodes: List<Episode>? = null
)