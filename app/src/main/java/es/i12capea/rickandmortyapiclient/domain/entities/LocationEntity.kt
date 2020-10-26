package es.i12capea.rickandmortyapiclient.domain.entities

data class LocationEntity (
    val id : Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Int>
)