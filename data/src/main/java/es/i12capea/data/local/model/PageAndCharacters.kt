package es.i12capea.data.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class PageAndCharacters(
    @Embedded
    val page: LocalCharacterPage,
    @Relation(
        parentColumn = "actualPage",
        entityColumn = "pageId"
    )
    val characters: List<LocalCharacter>
)