package es.i12capea.rickypedia.features.characters.character_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Page

data class CharacterListViewState(
    var lastPage : Page<Character>? = null,
    var characters: List<Character>? = null,
    var layoutManagerState: Parcelable? = null
)