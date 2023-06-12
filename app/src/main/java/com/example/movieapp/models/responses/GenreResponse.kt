package com.example.movieapp.models.responses

import android.os.Parcelable
import com.example.movieapp.models.Genre
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreResponse(
    @SerializedName("genres")
    val genres : List<Genre>

) : Parcelable {
    constructor() : this (mutableListOf())
}