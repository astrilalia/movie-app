package com.example.movieapp.models.responses

import android.os.Parcelable
import com.example.movieapp.models.Movie
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieResponse(
    @SerializedName("results")
    val movies : List<Movie>

) : Parcelable {
    constructor() : this (mutableListOf())
}