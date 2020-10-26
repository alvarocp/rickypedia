package es.i12capea.rickandmortyapiclient.presentation.characters.character_list.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Page

data class CharacterListViewState(
    var lastPage : Page<Character>? = null,
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null
)