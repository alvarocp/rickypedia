package es.i12capea.rickypedia.data.api

import es.i12capea.rickypedia.data.api.models.PageableResponse
import es.i12capea.rickypedia.data.api.models.location.RemoteLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {
    @GET("location/")
    fun getLocations(@Query("page") page: Int?): Call<PageableResponse<RemoteLocation>>

    @GET("location/{locationId}")
    fun getLocation(@Path("locationId") locationId: Int): Call<RemoteLocation>
}