package es.i12capea.rickandmortyapiclient.presentation.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode (
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<Int>
) : Parcelable