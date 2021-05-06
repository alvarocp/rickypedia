package es.i12capea.data.api
import es.i12capea.rickypedia.shared.data.api.models.PageableResponse
import es.i12capea.rickypedia.shared.data.api.models.character.RemoteCharacter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {
    @GET("character/")
    fun getCharactersAtPage(@Query("page") page: Int? = null) : Call<PageableResponse<RemoteCharacter>>

    @GET("character/{characterIds}")
    fun getCharacters(@Path(value = "characterIds", encoded = false) characterIds: List<Int>) : Call<List<RemoteCharacter>>

    @GET("character/{characterId}")
    fun getCharacter(@Path("characterId") characterId: Int) : Call<RemoteCharacter>

}