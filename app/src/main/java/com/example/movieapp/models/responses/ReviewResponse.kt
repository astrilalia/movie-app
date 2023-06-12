package com.example.movieapp.models.responses

import android.os.Parcelable
import com.example.movieapp.models.Review
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResponse(
    @SerializedName("results")
    val reviews: List<Review>

) : Parcelable {
    constructor() : this(mutableListOf())
}