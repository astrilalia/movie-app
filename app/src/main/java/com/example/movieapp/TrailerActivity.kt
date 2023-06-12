package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.movieapp.databinding.ActivityTrailerBinding

class TrailerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrailerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("videoUrl")
        videoUrl?.let {
            loadVideoUrl(it)
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

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }

}