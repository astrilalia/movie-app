package com.example.movieapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.adapter.GenreAdapter
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentGenreBinding
import com.example.movieapp.models.Genre
import com.example.movieapp.models.responses.GenreResponse
import com.example.movieapp.services.MovieApiInterface
import com.example.movieapp.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreFragment : Fragment() {

    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentGenreBinding.inflate(inflater, container, false)

        loadRVGenreList()

        return binding.root
    }

    private fun getGenreData(callback: (List<Genre>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getGenreList().enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful) {
                    val genreResponse = response.body()
                    if (genreResponse != null) {
                        Log.i("genre_fragment", genreResponse.toString())
                        return callback(response.body()!!.genres)
                    } else {
                        showError("Empty response body")
                    }
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    showError("Error $errorCode : $errorMessage")
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                showError("Network or API error: ${t.message}")
            }

        })
    }

    private fun loadRVGenreList() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)

        binding.rvGenreList.layoutManager = layoutManager
        binding.rvGenreList.setHasFixedSize(true)
        getGenreData { genres: List<Genre> ->
            val genreAdapter = GenreAdapter(genres)
            binding.rvGenreList.adapter = genreAdapter
            genreAdapter.onItemClick = {
                //bundle
                val genreId = it.id
                val bundle = Bundle()
                val movieFr = MovieFragment()
                bundle.putString("GENRE_ID", genreId)
                movieFr.arguments = bundle

                //navigation
                val fr = parentFragmentManager.beginTransaction().replace(R.id.fl_fragment, movieFr)
                fr.commit()
            }
        }
    }

    private fun showError(errorMessage: String) {
        println("Error: $errorMessage")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}