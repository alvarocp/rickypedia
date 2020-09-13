package es.i12capea.rickandmortyapiclient.presentation.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
    val episode: List<Int>
) : Parcelable{
}