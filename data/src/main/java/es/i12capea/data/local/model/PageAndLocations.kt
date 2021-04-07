package es.i12capea.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class PageAndLocations(
    @Embedded
    val page: LocalLocationPage,
    @Relation(
        parentColumn = "actualPage",
        entityColumn = "pageId"
    )
    val locations: List<LocalLocation>
)