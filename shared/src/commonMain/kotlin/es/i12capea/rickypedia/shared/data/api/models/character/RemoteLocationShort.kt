package es.i12capea.rickypedia.shared.data.api.models.character

import kotlinx.serialization.Serializable


@Serializable
data class RemoteLocationShort(
    val name : String,
    val url: String
)