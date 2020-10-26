package es.i12capea.rickandmortyapiclient.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_location")
data class LocalLocation (
    @PrimaryKey(autoGenerate = false)
    val id : Int,
    val pageId: Int?,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Int>
)