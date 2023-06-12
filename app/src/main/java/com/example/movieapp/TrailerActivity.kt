package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.movieapp.databinding.ActivityTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class TrailerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrailerBinding
//    private val youtubePlayerView = YouTubePlayerView(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("videoUrl")
        videoUrl?.let {
            loadVideoUrl(videoUrl)
//            loadTrailerUrl(videoUrl)
        }
    }

    private fun loadVideoUrl(videoUrl: String) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.i("trailer_activity", "this is trailer activity")
            }
        }
        binding.webView.loadUrl(videoUrl)
    }

//    private fun loadTrailerUrl(videoUrl: String) {
//        youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                super.onReady(youTubePlayer)
//                youTubePlayer.cueVideo(videoUrl,0f)
//            }
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }

}