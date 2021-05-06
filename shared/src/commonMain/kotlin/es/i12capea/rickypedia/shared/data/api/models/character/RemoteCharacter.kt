package es.i12capea.rickypedia.shared.data.api.models.character

import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: RemoteLocationShort,
    val location : RemoteLocationShort,
    val image: String,
    val episode: ArrayList<String>,
    val url: String,
    val created: String
)