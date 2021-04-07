package es.i12capea.data.api.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
) : Parcelable