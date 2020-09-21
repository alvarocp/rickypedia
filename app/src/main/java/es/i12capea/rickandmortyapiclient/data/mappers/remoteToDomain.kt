package es.i12capea.rickandmortyapiclient.data.mappers

import android.net.Uri
import es.i12capea.rickandmortyapiclient.data.api.models.PageableResponse
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter
import es.i12capea.rickandmortyapiclient.data.api.models.episode.RemoteEpisode
import es.i12capea.rickandmortyapiclient.data.api.models.location.RemoteLocation
import es.i12capea.rickandmortyapiclient.domain.entities.*
import java.net.URL

fun PageableResponse<RemoteCharacter>.characterPageToDomain() : PageEntity<CharacterEntity>{
    val next = getIdFromPage(this.info.next)
    val prev = getIdFromPage(this.info.prev)

    var actual = -1
    next?.let {
        actual = it -1
    }
    prev?.let {
        actual = it +1
    }

    return PageEntity(
        next,
        prev,
        actual,
        this.results.charactersToDomain()
    )
}

fun PageableResponse<RemoteEpisode>.episodePageToDomain() : PageEntity<EpisodeEntity>{
    val next = getIdFromPage(this.info.next)
    val prev = getIdFromPage(this.info.prev)

    var actual = -1
    next?.let {
        actual = it -1
    }
    prev?.let {
        actual = it +1
    }

    return PageEntity(
        next,
        prev,
        actual,
        this.results.episodesToDomain()
    )
}

fun PageableResponse<RemoteLocation>.locationPageToDomain() : PageEntity<LocationEntity> {

    val next = getIdFromPage(this.info.next)
    val prev = getIdFromPage(this.info.prev)

    var actual = -1
    next?.let {
        actual = it -1
    }
    prev?.let {
        actual = it +1
    }

    return PageEntity (
        next,
        prev,
        actual,
        this.results.locationsToDomain()
    )
}

fun getIdFromPage(url: String?) : Int?{
    return if (url.isNullOrEmpty()){
        null
    } else{
        val uri = Uri.parse(url)
        uri.getQueryParameter("page")?.toInt()
    }
}

fun getIdFromUrl(url: String?) : Int?{

    return if (url.isNullOrEmpty()){
        null
    } else{
        val index = url.lastIndexOf('/')
        url.substring(index +1 , url.length).toInt()
    }
}

fun List<RemoteEpisode>.episodesToDomain() : List<EpisodeEntity>{
    val episodeList = ArrayList<EpisodeEntity>()
    for(episode in this){
        episodeList.add(episode.toDomain())
    }
    return episodeList
}

fun List<RemoteCharacter>.charactersToDomain() : List<CharacterEntity>{
    val characterList = ArrayList<CharacterEntity>()
    for(character in this){
        characterList.add(character.toDomain())
    }
    return characterList
}

fun List<RemoteLocation>.locationsToDomain() : List<LocationEntity>{
    val locationList = ArrayList<LocationEntity>()
    for(location in this){
        locationList.add(location.toDomain())
    }
    return locationList
}

fun RemoteLocation.toDomain() : LocationEntity {

    val characterListInt = ArrayList<Int>()
    for (resident in residents){
        getIdFromUrl(resident)?.let {
            characterListInt.add(it)
        }
    }
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = characterListInt
    )
}

fun RemoteEpisode.toDomain() : EpisodeEntity {

    val characterListInt = ArrayList<Int>()

    for (c in characters){
        getIdFromUrl(c)?.let {
            characterListInt.add(it)
        }
    }

    return EpisodeEntity(
        id = id,
        name = name,
        air_date = air_date,
        episode = episode,
        characters = characterListInt
    )
}

fun RemoteCharacter.toDomain() : CharacterEntity {
    val episodes = ArrayList<Int>()
    for (e in episode){
        getIdFromUrl(e)?.let {
            episodes.add(it)
        }
    }

    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationShortEntity(getIdFromUrl(origin.url),origin.name),
        location = LocationShortEntity(getIdFromUrl(location.url), location.name),
        image = image,
        episode = episodes
    )
}