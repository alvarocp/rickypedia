package es.i12capea.rickandmortyapiclient.presentation.locations.location_list.state

sealed class LocationListStateEvent {
    class GetNextLocationPage() : LocationListStateEvent()
}