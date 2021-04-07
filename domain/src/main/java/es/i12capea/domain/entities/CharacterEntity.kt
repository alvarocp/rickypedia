package es.i12capea.domain.entities


data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationShortEntity,
    val location : LocationShortEntity,
    val image: String,
    val episodes: List<Int>
)