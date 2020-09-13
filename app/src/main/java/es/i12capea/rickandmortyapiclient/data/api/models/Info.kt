package es.i12capea.rickandmortyapiclient.data.api.models

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
) {
}