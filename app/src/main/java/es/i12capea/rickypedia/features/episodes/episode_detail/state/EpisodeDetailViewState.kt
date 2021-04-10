package es.i12capea.rickypedia.features.episodes.episode_detail.state

import android.os.Parcelable
import es.i12capea.rickypedia.common.BaseViewState
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode

data class EpisodeDetailViewState(
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null,
    var episode: Episode? = null
) : BaseViewState()