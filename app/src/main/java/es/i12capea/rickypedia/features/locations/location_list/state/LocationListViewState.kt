package es.i12capea.rickypedia.features.locations.location_list.state

import android.os.Parcelable
import es.i12capea.rickypedia.entities.Location
import es.i12capea.rickypedia.entities.Page

data class LocationListViewState(
    var locations: List<Location>? = null,
    var lastPage: Page<Location>? = null,
    var layoutManagerState: Parcelable? = null
)