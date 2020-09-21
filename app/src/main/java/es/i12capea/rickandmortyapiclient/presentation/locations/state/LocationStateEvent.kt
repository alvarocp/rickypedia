package es.i12capea.rickandmortyapiclient.presentation.locations.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Location

sealed class LocationStateEvent {
    class GetNextLocationPage() : LocationStateEvent()
    class GetCharactersInLocation(val location: Location) : LocationStateEvent()
}