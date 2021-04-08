package es.i12capea.rickypedia.features.locations.location_detail.state

import es.i12capea.rickypedia.entities.Location

sealed class LocationDetailStateEvent {
    class GetCharactersInLocation(val location: Location) : LocationDetailStateEvent()
}