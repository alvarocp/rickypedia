package es.i12capea.rickandmortyapiclient.presentation.locations.location_detail.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Location

data class LocationDetailViewState(
    var characters: List<Character> ? = null,
    var location: Location? = null
)