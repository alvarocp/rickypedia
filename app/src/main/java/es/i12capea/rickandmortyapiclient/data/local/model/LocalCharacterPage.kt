package es.i12capea.rickandmortyapiclient.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import es.i12capea.rickandmortyapiclient.data.api.models.Info
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "local_character_page")
data class LocalCharacterPage(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val info: Info,
    val results: List<RemoteCharacter>
) : Parcelable