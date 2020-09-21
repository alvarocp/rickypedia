package es.i12capea.rickandmortyapiclient.presentation.characters.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Episode
import es.i12capea.rickandmortyapiclient.presentation.entities.Page

data class CharactersViewState(
    var lastPage : Page<Character>? = Page(
        1,
        null,
        0,
        emptyList()
    ),
    var characters: List<Character>? = null,
    var character: Character? = null,
    var layoutManagerState: Parcelable? = null,
    var episodes: List<Episode>? = null
)