package com.example.movieapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trailer(
    @SerializedName("id")
    val id : String?,

    @SerializedName("key")
    val key : String?

) : Parcelable {
    constructor() : this("", "")
}