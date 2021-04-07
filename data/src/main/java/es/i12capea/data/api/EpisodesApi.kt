package es.i12capea.data.api

import es.i12capea.data.api.models.PageableResponse
import es.i12capea.data.api.models.episode.RemoteEpisode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {
    @GET("episode/")
    fun getAllEpisodes(@Query("page") page: Int? = null): Call<PageableResponse<RemoteEpisode>>

    @GET("episode/{episodeIds}")
    fun getEpisodes(@Path("episodeIds") episodeIds: List<Int>): Call<List<RemoteEpisode>>

    @GET("episode/{episodeId}")
    fun getEpisode(@Path("episodeId") episodeId: Int):  Call<RemoteEpisode>
}