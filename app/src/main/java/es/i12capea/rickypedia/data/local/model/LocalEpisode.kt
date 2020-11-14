package es.i12capea.rickypedia.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "local_episode")
@Parcelize
data class LocalEpisode(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val pageId: Int?,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<Int>
) : Parcelable{
}