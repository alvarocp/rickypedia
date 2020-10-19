package es.i12capea.rickandmortyapiclient.presentation.entities

data class Page<T>(
    val next : Int?,
    val prev: Int?,
    val actual: Int,
    val count: Int,
    val list: List<T>
) {

}