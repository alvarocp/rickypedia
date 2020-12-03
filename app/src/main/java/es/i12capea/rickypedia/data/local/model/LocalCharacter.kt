package es.i12capea.rickypedia.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "local_character")
data class LocalCharacter(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val pageId: Int?,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocalLocationShort,
    val location : LocalLocationShort,
    val image: String,
    val episodes: List<Int>
) : Parcelable