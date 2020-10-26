package es.i12capea.rickandmortyapiclient.presentation.locations.location_list.state

import android.os.Parcelable
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.entities.Page

data class LocationListViewState(
    var locations: List<Location>? = null,
    var lastPage: Page<Location>? = null,
    var layoutManagerState: Parcelable? = null
)