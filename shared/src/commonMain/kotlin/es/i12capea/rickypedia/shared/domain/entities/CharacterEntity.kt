package es.i12capea.rickypedia.shared.domain.entities


data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity,
    val location : es.i12capea.rickypedia.shared.domain.entities.LocationShortEntity,
    val image: String,
    val episodes: List<Int>
)