package es.i12capea.rickypedia.presentation.characters.character_detail.state

import es.i12capea.rickypedia.presentation.entities.Character
import es.i12capea.rickypedia.presentation.entities.Episode

data class CharacterDetailViewState(
    var character: Character? = null,
    var episodes: List<Episode>? = null,
    var isImageLoaded: Boolean? = null
)