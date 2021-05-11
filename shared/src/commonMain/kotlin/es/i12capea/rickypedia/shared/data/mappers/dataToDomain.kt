package es.i12capea.rickypedia.shared.data.mappers

import es.i12capea.rickypedia.shared.data.local.model.LocalCharacter
import es.i12capea.rickypedia.shared.data.local.model.LocalLocationShort
import es.i12capea.rickypedia.shared.domain.entities.CharacterEntity
import es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity

fun LocalLocationShort.toDomain() : LocationShortEntity {
    return LocationShortEntity(
        id = this.locationId,
        name = this.name
    )
}

fun LocalCharacter.toDomain() : CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin.toDomain(),
        location = location.toDomain(),
        image = image,
        episodes = episodes
    )
}

fun List<LocalCharacter>.toDomainCharacters() : List<CharacterEntity> {
    val arrayList = ArrayList<CharacterEntity>()
    for (item in this){
        arrayList.add(item.toDomain())
    }
    return arrayList
}