package es.i12capea.rickypedia.features.locations.location_detail.state

import es.i12capea.rickypedia.shared.domain.common.Constants
import es.i12capea.rickypedia.common.ErrorRym
import es.i12capea.rickypedia.common.Event
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Location

data class LocationDetailViewState(
    val characters: List<Character> ? = null,
    val location: Location? = null,
    val isLoading: Boolean = false,
    val errorRym: Event<ErrorRym> = Event(ErrorRym(Constants.NO_ERROR, ""))
)