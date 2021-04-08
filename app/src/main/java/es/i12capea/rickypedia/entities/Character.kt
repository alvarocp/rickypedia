package es.i12capea.rickypedia.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationShort,
    val location : LocationShort,
    val image: String,
    val episodes: List<Int>
) : Parcelable{
}