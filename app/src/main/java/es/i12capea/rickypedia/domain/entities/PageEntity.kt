package es.i12capea.rickypedia.domain.entities

data class PageEntity<T>(
    val nextPage: Int?,
    val prevPage: Int?,
    val actualPage: Int,
    val count: Int,
    val list: List<T>
)