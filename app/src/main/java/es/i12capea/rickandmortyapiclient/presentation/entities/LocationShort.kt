package es.i12capea.rickandmortyapiclient.presentation.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationShort(
    val id: Int?,
    val name: String
) : Parcelable