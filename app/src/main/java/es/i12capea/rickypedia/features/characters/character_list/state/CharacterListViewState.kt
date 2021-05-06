package es.i12capea.rickypedia.features.characters.character_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.shared.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Page

data class CharacterListViewState(
    val lastPage : Page<Character>? = null,
    val characters: List<Character>? = null,
    val layoutManagerState: Parcelable? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)