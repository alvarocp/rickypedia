package es.i12capea.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalLocationShort(
    val locationId: Int?,
    val name: String
) : Parcelable