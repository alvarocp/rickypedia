package es.i12capea.rickypedia.features.episodes.episode_detail.state

import android.os.Parcelable
import es.i12capea.rickypedia.shared.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode

data class EpisodeDetailViewState(
    val characters: List<Character>? = null,
    val layoutManagerState: Parcelable? = null,
    val episode: Episode? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)