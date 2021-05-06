package es.i12capea.rickypedia.shared.data.api

import es.i12capea.rickypedia.shared.data.api.models.PageableResponse
import es.i12capea.rickypedia.shared.data.api.models.location.RemoteLocation

class LocationApi(){
    suspend fun getLocationsAtPage(page: Int? = 1) : PageableResponse<RemoteLocation> {
        return ApiClient.getResponse("location")
    }

    suspend fun getLocation(locationId: Int) : RemoteLocation  {
        return ApiClient.getResponse("location/$locationId")
    }

}