package es.i12capea.domain.entities

data class EpisodeEntity(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<Int>
)