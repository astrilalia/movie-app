package com.example.movieapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movieapp.R
import com.example.movieapp.TrailerActivity
import com.example.movieapp.adapter.ReviewAdapter
import com.example.movieapp.databinding.FragmentMovieDetailBinding
import com.example.movieapp.models.Review
import com.example.movieapp.models.Trailer
import com.example.movieapp.models.responses.DetailResponse
import com.example.movieapp.models.responses.ReviewResponse
import com.example.movieapp.models.responses.TrailerResponse
import com.example.movieapp.services.MovieApiInterface
import com.example.movieapp.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var backPressedCallback: OnBackPressedCallback

    /* **Lifecycle** */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        getDetailData()
        getTrailerData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fr = parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment, MovieFragment())
                fr.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onStop() {
        super.onStop()
        backPressedCallback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* **Functions** */

    private fun getDetailData() {
        val args = this.arguments
        val movId = args?.get("MOVIE_ID")
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        val call = apiService.getDetail(movId.toString())

        call.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    val detailResponse = response.body()
                    if (detailResponse != null) {
                        Log.i("movie_detail_fragment", detailResponse.toString())
                        binding.movieDetailTitle.text = detailResponse.title
                        binding.releaseDate.text = detailResponse.release
                        Glide.with(binding.root).load(IMAGE_BASE + detailResponse.poster)
                            .into(binding.movieDetailPoster)
                        Glide.with(binding.root)
                            .load(IMAGE_BASE + detailResponse.poster)
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.ivTrailer)
                        binding.tvDetailOverview.text = detailResponse.overview
                        binding.movieDetailPoster.setOnClickListener {
                            val bundle = Bundle()
                            val reviewFragment = ReviewFragment()
                            bundle.putString("MOVIE_ID", movId.toString())
                            reviewFragment.arguments = bundle

                            val fr = parentFragmentManager.beginTransaction()
                                .replace(R.id.fl_fragment, reviewFragment)
                            fr.commit()
                        }

                    } else {
                        showError("Empty response body")
                    }
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    showError("Error $errorCode : $errorMessage")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                showError("Network or API error: ${t.message}")
            }

        })
    }

    private fun getTrailerData() {
        val args = this.arguments
        val movId = args?.get("MOVIE_ID")
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        val call = apiService.getTrailer(movId.toString())

        call.enqueue(object : Callback<TrailerResponse> {
            override fun onResponse(
                call: Call<TrailerResponse>,
                response: Response<TrailerResponse>
            ) {
                if (response.isSuccessful) {
                    val trailerResponse = response.body()
                    if (trailerResponse != null) {
                        val trailers = trailerResponse.trailers
                        val key = trailers[0].key

                        binding.ivTrailer.setOnClickListener {
                            openVideoPlayer(it.context, "https://www.youtube.com/watch?v=${key}")
                        }

                    } else {
                        showError("Empty response body")
                    }
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    showError("Error $errorCode : $errorMessage")
                }
            }

            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                showError("Network or API error: ${t.message}")
            }

        })

    }

    private fun openVideoPlayer(context: Context, videoUrl: String) {
        val intent = Intent(context, TrailerActivity::class.java)
        intent.putExtra("videoUrl", videoUrl)
        context.startActivity(intent)
    }

    private fun showError(errorMessage: String) {
        println("Error: $errorMessage")
    }

}