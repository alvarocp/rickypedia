package es.i12capea.rickypedia.features.locations.location_detail.state

import es.i12capea.rickypedia.common.BaseViewState
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Location

data class LocationDetailViewState(
    var characters: List<Character> ? = null,
    var location: Location? = null
) : BaseViewState()