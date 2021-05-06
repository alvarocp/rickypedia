package es.i12capea.rickypedia.shared.data.api.models

import kotlinx.serialization.Serializable


@Serializable
data class Results <T> (
    val results: List<T>
)