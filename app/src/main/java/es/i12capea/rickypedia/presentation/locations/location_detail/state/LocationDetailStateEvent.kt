package es.i12capea.rickypedia.presentation.locations.location_detail.state

import es.i12capea.rickypedia.presentation.entities.Location

sealed class LocationDetailStateEvent {
    class GetCharactersInLocation(val location: Location) : LocationDetailStateEvent()
}