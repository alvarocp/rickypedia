package es.i12capea.data.api.models.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RemoteLocationShort(
    val name : String,
    val url: String
) : Parcelable