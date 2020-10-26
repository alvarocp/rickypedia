package es.i12capea.rickandmortyapiclient.presentation.locations.location_detail.state

import es.i12capea.rickandmortyapiclient.presentation.entities.Location

sealed class LocationDetailStateEvent {
    class GetCharactersInLocation(val location: Location) : LocationDetailStateEvent()
}