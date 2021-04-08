package es.i12capea.rickypedia.features.locations.location_list.state

sealed class LocationListStateEvent {
    class GetNextLocationPage() : LocationListStateEvent()
}