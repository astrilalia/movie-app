package com.example.movieapp.models.responses

import android.os.Parcelable
import com.example.movieapp.models.Trailer
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailerResponse(
    @SerializedName("results")
    val trailers : List<Trailer>

) : Parcelable {
    constructor() : this(mutableListOf())
}