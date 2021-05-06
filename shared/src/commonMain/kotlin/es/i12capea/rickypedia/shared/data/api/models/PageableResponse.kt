package es.i12capea.rickypedia.shared.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PageableResponse<T>(
    val info: Info,
    val results: List<T>
)