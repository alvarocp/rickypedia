package es.i12capea.rickypedia.features.characters.character_detail.state

import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode

data class CharacterDetailViewState(
    var character: Character? = null,
    var episodes: List<Episode>? = null,
    var isImageLoaded: Boolean? = null
)