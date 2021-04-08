package es.i12capea.rickypedia.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationShort(
    val id: Int?,
    val name: String
) : Parcelable