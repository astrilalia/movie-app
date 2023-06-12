package com.example.movieapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    @SerializedName("title")
    val title : String?,

    @SerializedName("poster_path")
    val poster : String?,

    @SerializedName("release_date")
    val release : String?,

    @SerializedName("overview")
    val overview : String?,

    @SerializedName("video")
    val trailer : Boolean?

) : Parcelable {
    constructor() : this("", "", "", "", false)
}