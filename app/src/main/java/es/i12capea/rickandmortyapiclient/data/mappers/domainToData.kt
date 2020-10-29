package es.i12capea.rickandmortyapiclient.data.mappers

import es.i12capea.rickandmortyapiclient.data.local.model.*
import es.i12capea.rickandmortyapiclient.domain.entities.CharacterEntity
import es.i12capea.rickandmortyapiclient.domain.entities.EpisodeEntity
import es.i12capea.rickandmortyapiclient.domain.entities.LocationEntity
import es.i12capea.rickandmortyapiclient.domain.entities.PageEntity


fun LocationEntity.toLocal(pageId: Int?) : LocalLocation{
    return LocalLocation(
        id = id,
        pageId= pageId,
        name= name,
        type= type,
        dimension= dimension,
        residents= residents
    )
}

fun List<LocationEntity>.listLocationEntityToLocal(page: Int?) : List<LocalLocation>{
    val list = ArrayList<LocalLocation>()
    for (location in this){
        list.add(location.toLocal(page))
    }
    return list
}

fun PageEntity<LocationEntity>.toLocal() : LocalLocationPage{
    return LocalLocationPage(
        actualPage = actualPage,
        nextPage = nextPage,
        prevPage = prevPage,
        count = count
    )
}

fun List<CharacterEntity>.listCharacterEntityToLocal(page: Int?) : List<LocalCharacter>{
    val list = ArrayList<LocalCharacter>()
    for (character in this){
        list.add(character.toLocal(page))
    }
    return list
}

fun PageEntity<CharacterEntity>.toLocal() : PageAndCharacters {
    return PageAndCharacters(
        page = this.toLocalPage(),
        characters = this.toLocalCharacters()
    )
}

fun PageEntity<CharacterEntity>.toLocalPage() : LocalCharacterPage {
    return LocalCharacterPage(
        actualPage = actualPage,
        nextPage = nextPage,
        prevPage = prevPage,
        count = count
    )
}

fun PageEntity<CharacterEntity>.toLocalCharacters() : List<LocalCharacter>{
    val list = ArrayList<LocalCharacter>()
    for (character in this.list){
        list.add(character.toLocal(this.actualPage))
    }
    return list
}

fun CharacterEntity.toLocal(page: Int?) : LocalCharacter {
    return LocalCharacter(
        id = id,
        pageId = page,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocalLocationShort(origin.id, name),
        location  = LocalLocationShort(location.id, location.name),
        image = image,
        episodes = episodes

    )
}


fun EpisodeEntity.toLocal(page: Int?) : LocalEpisode {
    return LocalEpisode(
        id= id ,
        pageId = page,
        name= name,
        air_date= air_date,
        episode= episode,
        characters= characters
    )
}

fun List<EpisodeEntity>.toLocal(page: Int?) : List<LocalEpisode>{
    val list = ArrayList<LocalEpisode>()
    for (episode in this){
        list.add(episode.toLocal(page))
    }
    return list
}

fun PageEntity<EpisodeEntity>.toLocalEpisodePage() : LocalEpisodePage {
    return LocalEpisodePage(
        actualPage = actualPage,
        nextPage = nextPage,
        prevPage = prevPage,
        count = count
    )
}