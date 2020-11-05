package es.i12capea.rickypedia.presentation.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationShort(
    val id: Int?,
    val name: String
) : Parcelable