package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ReviewItemBinding
import com.example.movieapp.models.Review

class ReviewAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        with(holder) {
            with(reviews[position]) {
                binding.tvAuthor.text = this.author
                binding.contentReview.text = this.content
//                Glide.with(binding.root).load(IMAGE_BASE + this.avatar_path).into(binding.ivAvatar)
            }
        }
    }

}