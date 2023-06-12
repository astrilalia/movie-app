package com.example.movieapp.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.models.Movie
import com.example.movieapp.models.responses.MovieResponse
import com.example.movieapp.services.MovieApiInterface
import com.example.movieapp.services.MovieApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val itemList = mutableListOf<Movie>()
    private val adapter = MovieAdapter(itemList)

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var backPressedCallback: OnBackPressedCallback

    /* **Lifecycle** */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)

        loadRVMovieList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvMoviesList
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        // Add a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//                if (d) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    currentPage++
                    fetchData()
                }
//                }
                Log.i("dy", dy.toString())
                Log.i("dx", dx.toString())
                Log.i("visible", visibleItemCount.toString())
                Log.i("total item", totalItemCount.toString())
                Log.i("first", firstVisibleItemPosition.toString())
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    override fun onStart() {
        super.onStart()
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fr = parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment, GenreFragment())
                fr.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onStop() {
        super.onStop()
        // Remove the callback when the fragment is stopped
        backPressedCallback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* **Functions** */

    private fun showError(errorMessage: String) {
        println("Error: $errorMessage")
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        //fetch data dari Genre Fragment
        val args = this.arguments
        val genId = args?.get("GENRE_ID")

        //api service
        val apiService = MovieApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList(genId.toString(), currentPage)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        if (movieResponse != null) {
                            Log.i("movie_fragment", movieResponse.toString())
                            return callback(response.body()!!.movies)
                        } else {
                            showError("Empty response body")
                        }
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        showError("Error $errorCode : $errorMessage")
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    showError("Network or API error: ${t.message}")
                }

            })
    }

    private fun loadRVMovieList() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        binding.rvMoviesList.layoutManager = layoutManager
        binding.rvMoviesList.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            val movieAdapter = MovieAdapter(movies)
            binding.rvMoviesList.adapter = movieAdapter
            movieAdapter.onItemClick = {
                val movieId = it.id
                val bundle = Bundle()
                val movieDetailFragment = MovieDetailFragment()
                bundle.putString("MOVIE_ID", movieId)
                movieDetailFragment.arguments = bundle

                val fr = parentFragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment, movieDetailFragment)
                fr.commit()
            }

        }

    }

    private fun fetchData() {
        isLoading = true

        getMovieData { movies: List<Movie> ->
            val prevSize = itemList.size
            itemList.addAll(movies)
//            movieAdapter.notifyItemRangeInserted(prevSize, movies.size)
            isLoading = false

//            isLastPage = movies.isEmpty()

        }

    }

}