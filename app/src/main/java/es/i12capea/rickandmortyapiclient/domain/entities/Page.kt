package es.i12capea.rickandmortyapiclient.domain.entities

data class Page<T>(
    val prev: Int?,
    val next: Int?,
    val list: List<T>
)