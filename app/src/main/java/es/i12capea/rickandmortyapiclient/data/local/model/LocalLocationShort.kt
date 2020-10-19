package es.i12capea.rickandmortyapiclient.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocalLocationShort(
    val locationId: Int?,
    val name: String
) : Parcelable