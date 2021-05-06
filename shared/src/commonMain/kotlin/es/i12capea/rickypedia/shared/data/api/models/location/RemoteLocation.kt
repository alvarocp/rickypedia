package es.i12capea.rickypedia.shared.data.api.models.location

import kotlinx.serialization.Serializable

@Serializable
data class RemoteLocation(
    val id : Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: ArrayList<String>,
    val url: String,
    val created: String
)