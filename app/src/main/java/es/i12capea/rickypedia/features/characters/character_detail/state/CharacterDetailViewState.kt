package es.i12capea.rickypedia.features.characters.character_detail.state

import es.i12capea.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode

data class CharacterDetailViewState(
    val character: Character? = null,
    val episodes: List<Episode>? = null,
    val isImageLoaded: Boolean? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, "")),
)