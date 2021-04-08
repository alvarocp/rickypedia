package es.i12capea.rickypedia.features.episodes.episode_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.Page

data class EpisodeListViewState(
    var lastPage: Page<Episode>? = null,
    var layoutManagerState: Parcelable? = null,
    var episodes: List<Episode>? = null
)