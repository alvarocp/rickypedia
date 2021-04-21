package es.i12capea.rickypedia.features.locations.location_detail.state

import es.i12capea.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Location

data class LocationDetailViewState(
    var characters: List<Character> ? = null,
    var location: Location? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)