package es.i12capea.rickypedia.shared.data.api

import es.i12capea.rickypedia.shared.data.api.models.PageableResponse
import es.i12capea.rickypedia.shared.data.api.models.character.RemoteCharacter
import es.i12capea.rickypedia.shared.data.api.models.episode.RemoteEpisode

class EpisodesApi() {
    suspend fun getCharactersAtPage(page: Int? = 1) : PageableResponse<RemoteEpisode>{
        return ApiClient.getResponse("episode", listOf(Pair("page", page.toString())))
    }

    suspend fun getCharacters(characterIds : List<Int>) : List<RemoteCharacter>{
        return ApiClient.getResponse("character/$characterIds")
    }

    suspend fun getCharacter(characterId: Int) : RemoteCharacter {
        return ApiClient.getResponse("character/$characterId")
    }
}