package es.i12capea.rickypedia.presentation.entities.mappers

import es.i12capea.domain.entities.CharacterEntity
import es.i12capea.domain.entities.EpisodeEntity
import es.i12capea.domain.entities.LocationEntity
import es.i12capea.domain.entities.PageEntity
import es.i12capea.rickypedia.presentation.entities.*

fun PageEntity<CharacterEntity>.characterPageEntityToPresentation() : Page<Character>{
    return Page(
        this.nextPage,
        this.prevPage,
        this.actualPage,
        count,
        this.list.characterListToPresentation()
    )
}

fun PageEntity<LocationEntity>.locationPageEntityToPresentation() : Page<Location>{
    return Page(
        this.nextPage,
        this.prevPage,
        this.actualPage,
        count,
        this.list.locationListToPresentation()
    )
}

fun List<CharacterEntity>.characterListToPresentation() : List<Character>{
    val characterList = ArrayList<Character>()
    for(ch in this){
        characterList.add(ch.toPresentation())
    }
    return characterList
}

fun PageEntity<EpisodeEntity>.episodePageToPresentation() : Page<Episode>{
    return Page(
        this.nextPage,
        this.prevPage,
        this.actualPage,
        count,
        this.list.episodeListToPresentation()
    )
}

fun List<EpisodeEntity>.episodeListToPresentation() : List<Episode>{
    val episodeList = ArrayList<Episode>()
    for(ep in this){
        episodeList.add(ep.toPresentation())
    }
    return episodeList
}

fun List<LocationEntity>.locationListToPresentation() : List<Location>{
    val locationList = ArrayList<Location>()
    for(loc in this){
        locationList.add(loc.toPresentation())
    }
    return locationList
}

fun CharacterEntity.toPresentation() : Character{
    return Character(
        id= id,
        name= name,
        status= status,
        species= species,
        type= type,
        gender= gender,
        origin= LocationShort(origin.id, origin.name),
        location = LocationShort(location.id, location.name),
        image= image,
        episodes= episodes
    )
}

fun EpisodeEntity.toPresentation() : Episode {
    return Episode(
        id= id,
        name= name,
        air_date= air_date,
        episode= episode,
        characters= characters
    )
}

fun LocationEntity.toPresentation() : Location {
    return Location(
        id = id,
        name= name,
        type = type,
        dimension = dimension,
        residents = residents
    )
}