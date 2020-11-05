package es.i12capea.rickypedia.data.api.models

data class PageableResponse<T>(
    val info: Info,
    val results: List<T>
) {
}