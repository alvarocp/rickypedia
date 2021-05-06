package es.i12capea.data.api.models

import es.i12capea.rickypedia.shared.data.api.models.Info

data class PageableResponse<T>(
    val info: Info,
    val results: List<T>
) {
}