package com.example.movieapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.ReviewAdapter
import com.example.movieapp.databinding.FragmentReviewBinding
import com.example.movieapp.models.Review
import com.example.movieapp.models.responses.ReviewResponse
import com.example.movieapp.services.MovieApiInterface
import com.example.movieapp.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        loadRVReviewList()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fr = parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment, MovieDetailFragment())
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

    private fun getReviewData(callback: (List<Review>) -> Unit) {
        val args = this.arguments
        val movId = args?.get("MOVIE_ID")
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        val call = apiService.getReviewList(movId.toString())

        call.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {
                if (response.isSuccessful) {
                    val reviewResponse = response.body()
                    if (reviewResponse != null) {
                        Log.i("review_fragment", reviewResponse.toString())
                        return callback(response.body()!!.reviews)

                    } else {
                        showError("Empty response body")
                    }
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    showError("Error $errorCode : $errorMessage")
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                showError("Network or API error: ${t.message}")
            }

        })

    }

    private fun loadRVReviewList() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        binding.rvReviewList.layoutManager = layoutManager
        binding.rvReviewList.setHasFixedSize(true)
        getReviewData { reviews: List<Review> ->
            val reviewAdapter = ReviewAdapter(reviews)
            binding.rvReviewList.adapter = reviewAdapter
        }
    }

    private fun showError(errorMessage: String) {
        println("Error: $errorMessage")
    }

}