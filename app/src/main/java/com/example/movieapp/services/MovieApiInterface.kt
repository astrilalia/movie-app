package com.example.movieapp.services

import com.example.movieapp.models.responses.DetailResponse
import com.example.movieapp.models.responses.GenreResponse
import com.example.movieapp.models.responses.MovieResponse
import com.example.movieapp.models.responses.ReviewResponse
import com.example.movieapp.models.responses.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {

    //Movie List
    @GET("/3/discover/movie?api_key=2686301a0291bac2d2d172e95a9cbf49")
    fun getMovieList(
        @Query("with_genres") with_genres: String,
        @Query("page") page: Int
    ) : Call<MovieResponse>

    //Genre List
    @GET("/3/genre/movie/list?api_key=2686301a0291bac2d2d172e95a9cbf49")
    fun getGenreList(): Call<GenreResponse>

    //Detail
    @GET("/3/movie/{movie_id}?api_key=2686301a0291bac2d2d172e95a9cbf49")
    fun getDetail(@Path("movie_id") movie_id: String): Call<DetailResponse>

    //Review List
    @GET("/3/movie/{movie_id}/reviews?api_key=2686301a0291bac2d2d172e95a9cbf49")
    fun getReviewList(@Path("movie_id") movie_id: String): Call<ReviewResponse>

    //Trailer List
    @GET("/3/movie/{movie_id}/videos?api_key=2686301a0291bac2d2d172e95a9cbf49")
    fun getTrailer(@Path("movie_id") movie_id: String): Call<TrailerResponse>

}