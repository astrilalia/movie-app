package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.models.Movie

class MovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    }

    var onItemClick: ((Movie) -> Unit)? = null
    val addMovie: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        with(holder) {
            with(movies[position]) {
                binding.movieTitle.text = this.title
                binding.movieRelease.text = this.release
                Glide.with(binding.root).load(IMAGE_BASE + this.poster).into(binding.moviePoster)
                binding.moviePoster.setOnClickListener {
                    onItemClick?.invoke(this)
                }
            }
        }
    }

    fun setMovie(data: List<Movie>) {
        addMovie.clear()
        addMovie.addAll(data)
        notifyDataSetChanged()
    }
}