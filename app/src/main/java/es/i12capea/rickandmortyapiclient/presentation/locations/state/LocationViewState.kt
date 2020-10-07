package es.i12capea.rickandmortyapiclient.presentation.locations.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Character
import es.i12capea.rickandmortyapiclient.presentation.entities.Location
import es.i12capea.rickandmortyapiclient.presentation.entities.Page

data class LocationViewState(
    var locations: List<Location>? = null,
    var lastPage: Page<Location>? = Page(
        1,
        null,
        0,
        emptyList()
    ),
    var characters: List<Character> ? = null,
    var location: Location? = null
)