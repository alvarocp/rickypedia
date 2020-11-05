package es.i12capea.rickypedia.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class PageAndEpisodes(
    @Embedded
    val page: LocalEpisodePage,
    @Relation(
        parentColumn = "actualPage",
        entityColumn = "pageId"
    )
    val episodes: List<LocalEpisode>
) {
}