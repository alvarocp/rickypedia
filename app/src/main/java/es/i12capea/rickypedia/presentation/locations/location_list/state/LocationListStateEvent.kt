package es.i12capea.rickypedia.presentation.locations.location_list.state

sealed class LocationListStateEvent {
    class GetNextLocationPage() : LocationListStateEvent()
}