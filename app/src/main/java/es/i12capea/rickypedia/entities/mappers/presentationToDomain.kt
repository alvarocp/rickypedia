package es.i12capea.rickypedia.entities.mappers

import es.i12capea.domain.entities.CharacterEntity
import es.i12capea.domain.entities.EpisodeEntity
import es.i12capea.domain.entities.LocationEntity
import es.i12capea.domain.entities.LocationShortEntity
import es.i12capea.rickypedia.entities.Character
import es.i12capea.rickypedia.entities.Episode
import es.i12capea.rickypedia.entities.Location

fun Character.toDomain() : CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = LocationShortEntity(
            origin.id,
            origin.name
        ),
        location = LocationShortEntity(
            location.id,
            location.name
        ),
        image = image,
        episodes = episodes
    )
}

fun Episode.toDomain() : EpisodeEntity {
    return EpisodeEntity(
        id= id,
        name= name,
        air_date= air_date,
        episode= episode,
        characters=characters
    )
}

fun Location.toDomain() : LocationEntity{
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents
    )
}