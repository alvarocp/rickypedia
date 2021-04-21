package es.i12capea.rickypedia.features.episodes.episode_list.state

import android.os.Parcelable
import es.i12capea.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.Page

data class EpisodeListViewState(
    val lastPage: Page<Episode>? = null,
    val layoutManagerState: Parcelable? = null,
    val episodes: List<Episode>? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)