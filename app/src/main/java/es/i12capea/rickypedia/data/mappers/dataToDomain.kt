package es.i12capea.rickypedia.data.mappers

import android.net.Uri
import es.i12capea.rickypedia.data.api.models.Info
import es.i12capea.rickypedia.data.api.models.PageableResponse
import es.i12capea.rickypedia.data.api.models.character.RemoteCharacter
import es.i12capea.rickypedia.data.api.models.episode.RemoteEpisode
import es.i12capea.rickypedia.data.api.models.location.RemoteLocation
import es.i12capea.rickypedia.data.local.model.*
import es.i12capea.rickypedia.domain.entities.*


fun PageAndLocations.toDomain() : PageEntity<LocationEntity>{
    return PageEntity(
        nextPage =  this.page.nextPage,
        prevPage = this.page.prevPage,
        actualPage = this.page.actualPage,
        count = this.page.count,
        list = this.locations.localLocationsToDomain()
    )
}

fun List<LocalLocation>.localLocationsToDomain() : List<LocationEntity>{
    val list = ArrayList<LocationEntity>()
    for (location in this){
        list.add(location.toDomain())
    }
    return list
}

fun LocalLocation.toDomain() : LocationEntity{
    return LocationEntity(
        id = id,
        name= name,
        type= type,
        dimension= dimension,
        residents= residents
    )
}

fun PageAndEpisodes.toDomain() : PageEntity<EpisodeEntity>{
    return PageEntity(
        nextPage =  this.page.nextPage,
        prevPage = this.page.prevPage,
        actualPage = this.page.actualPage,
        count = this.page.count,
        list = this.episodes.toDomain()
    )
}

fun List<LocalEpisode>.toDomain() : List<EpisodeEntity>{
    val list = ArrayList<EpisodeEntity>()
    for (episode in this){
        list.add(episode.toDomain())
    }
    return list
}

fun LocalEpisode.toDomain() : EpisodeEntity{
    return EpisodeEntity(
         id = id,
         name = name,
         air_date = air_date,
         episode = episode,
         characters = characters
    )
}

fun PageAndCharacters.toDomain() : PageEntity<CharacterEntity>{
    return PageEntity(
        nextPage =  this.page.nextPage,
        prevPage = this.page.prevPage,
        actualPage = this.page.actualPage,
        count = this.page.count,
        list = this.characters.localCharactersToDomain()
    )
}

fun LocalCharacter.toDomain() : CharacterEntity{

    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationShortEntity(origin.locationId ,origin.name),
        location = LocationShortEntity(location.locationId, location.name),
        image = image,
        episodes = this.episodes
    )
}

fun List<LocalCharacter>.localCharactersToDomain() : List<CharacterEntity>{
    val characterList = ArrayList<CharacterEntity>()
    for(character in this){
        characterList.add(character.toDomain())
    }
    return characterList
}



fun Info.getActualPage() : Int{
    val next = getIdFromPage(this.next)
    val prev = getIdFromPage(this.prev)

    var actual = -1
    next?.let {
        actual = it -1
    }
    prev?.let {
        actual = it +1
    }
    return actual
}

fun PageableResponse<RemoteCharacter>.characterPageToDomain() : PageEntity<CharacterEntity>{
    return PageEntity(
        nextPage =  getIdFromPage(this.info.next),
        prevPage = getIdFromPage(this.info.prev),
        actualPage =  info.getActualPage(),
        count = this.results.size,
        list =  this.results.charactersToDomain()
    )
}


fun PageableResponse<RemoteLocation>.locationPageToDomain() : PageEntity<LocationEntity> {
    return PageEntity(
        nextPage =  getIdFromPage(this.info.next),
        prevPage = getIdFromPage(this.info.prev),
        actualPage =  info.getActualPage(),
        count = this.results.size,
        list =  this.results.locationsToDomain()
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

fun PageableResponse<RemoteEpisode>.episodePageToDomain() : PageEntity<EpisodeEntity>{
    return PageEntity(
        nextPage =  getIdFromPage(this.info.next),
        prevPage = getIdFromPage(this.info.prev),
        actualPage =  info.getActualPage(),
        count = this.results.size,
        list =  this.results.episodesToDomain()
    )
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
    for (e in this.episode){
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
        episodes = episodes
    )
}

