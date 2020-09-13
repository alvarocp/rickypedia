package es.i12capea.rickandmortyapiclient.presentation.characters.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode

data class CharactersViewState(
    var page: Int = 1,
    var characters: List<Character>? = null,
    var character: Character? = null,
    var layoutManagerState: Parcelable? = null,
    var episodes: List<Episode>? = null
)