package es.i12capea.rickandmortyapiclient.data.api.models.character

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "remote_character")
data class RemoteCharacter(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: RemoteLocationShort,
    val location : RemoteLocationShort,
    val image: String,
    val episode: ArrayList<String>,
    val url: String,
    val created: String
) : Parcelable {

}