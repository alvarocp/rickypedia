package es.i12capea.rickypedia.features.locations.location_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.shared.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Location
import es.i12capea.rickypedia.entities.Page

data class LocationListViewState(
    val locations: List<Location>? = null,
    val lastPage: Page<Location>? = null,
    val layoutManagerState: Parcelable? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)