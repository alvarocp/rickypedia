package es.i12capea.rickandmortyapiclient.data.api.models.character

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RemoteLocationShort(
    val name : String,
    val url: String
) : Parcelable