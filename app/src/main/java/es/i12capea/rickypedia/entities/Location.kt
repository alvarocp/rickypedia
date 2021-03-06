package es.i12capea.rickypedia.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id : Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Int>
) : Parcelable