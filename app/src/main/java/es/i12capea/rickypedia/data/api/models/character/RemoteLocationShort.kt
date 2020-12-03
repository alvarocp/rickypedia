package es.i12capea.rickypedia.data.api.models.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RemoteLocationShort(
    val name : String,
    val url: String
) : Parcelable