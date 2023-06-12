package com.example.movieapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    @SerializedName("id")
    val id : String?,

    @SerializedName("author")
    val author : String?,

    @SerializedName("avatar_path")
    val avatar_path : String?,

    @SerializedName("rating")
    val rating : String?,

    @SerializedName("content")
    val content : String?,

    @SerializedName("created_at")
    val created_at : String?,

    @SerializedName("url")
    val url : String?

) : Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}