package es.i12capea.rickandmortyapiclient.data.api.models

data class PageableResponse<T>(
    val info: Info,
    val results: List<T>
) {
}