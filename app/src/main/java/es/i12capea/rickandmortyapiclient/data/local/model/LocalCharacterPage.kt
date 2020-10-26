package es.i12capea.rickandmortyapiclient.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "local_character_page")
data class LocalCharacterPage(
    @PrimaryKey(autoGenerate = false)
    val actualPage: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val count: Int
) : Parcelable