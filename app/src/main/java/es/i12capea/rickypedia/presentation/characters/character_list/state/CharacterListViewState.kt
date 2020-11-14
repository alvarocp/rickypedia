package es.i12capea.rickypedia.presentation.characters.character_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.presentation.entities.Character
import es.i12capea.rickypedia.presentation.entities.Page

data class CharacterListViewState(
    var lastPage : Page<Character>? = null,
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null
)