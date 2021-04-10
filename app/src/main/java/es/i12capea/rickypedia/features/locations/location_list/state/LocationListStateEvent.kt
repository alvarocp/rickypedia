package es.i12capea.rickypedia.features.locations.location_list.state

sealed class LocationListStateEvent {
    object GetNextLocationPage : LocationListStateEvent()
}