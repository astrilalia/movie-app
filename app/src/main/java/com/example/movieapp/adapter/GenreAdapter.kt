package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.GenreItemBinding
import com.example.movieapp.models.Genre

class GenreAdapter(
    private val genres: List<Genre>
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    inner class GenreViewHolder(val binding: GenreItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onItemClick: ((Genre) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        with(holder) {
            with(genres[position]) {
                binding.btnGenre.text = this.name
                binding.btnGenre.setOnClickListener {
                    onItemClick?.invoke(this)
                }
            }
        }
    }
}