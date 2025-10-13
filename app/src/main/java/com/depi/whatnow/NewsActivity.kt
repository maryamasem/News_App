package com.depi.whatnow

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.depi.whatnow.databinding.ActivityNewsBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var categorySelected: String
    private var country: String = "us"
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
        //Log.d("AdMob", "AdMob initialized")
        }
        loadAdBanner()

        setupRecycler()
        loadNews()

        binding.swipeRefresh.setOnRefreshListener { loadNews() }

    }
    private fun loadAdBanner() {
        categorySelected = intent.getStringExtra("category") ?: "general"
        pref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        country = pref.getString("country", "us") ?: "us"

        val adRequest = AdRequest.Builder()
            .addKeyword(country)
            .addKeyword(categorySelected)
            .build()
        //Log.d("AdMob", "Loading Ad...")
        binding.adView.loadAd(adRequest)
        //Log.d("AdMob", "Ad Loaded Request Sent")
    }

    private fun setupRecycler() {
        adapter = NewsAdapter { article ->
            val url = article.url
            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                Toast.makeText(this, "No article link found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.newsList.layoutManager = LinearLayoutManager(this)
        binding.newsList.adapter = adapter

    }

    private fun loadNews() {
        binding.progress.isVisible = true
        binding.tvError.isVisible = false

        categorySelected = intent.getStringExtra("category") ?: "general"
        pref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        country = pref.getString("country", "us") ?: "us"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NewsCallable::class.java)
        api.getNewsByCategory(category = categorySelected, country = country)
            .enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    binding.progress.isVisible = false
                    binding.swipeRefresh.isRefreshing = false

                    if (response.isSuccessful && response.body() != null) {
                        val articles = response.body()!!.articles
                        if (articles.isNotEmpty()) {
                            adapter.submitList(articles)
                            binding.newsList.isVisible = true
                        } else {
                            binding.tvError.isVisible = true
                            binding.tvError.text = "No news found for this category"
                        }
                    } else {
                        binding.tvError.isVisible = true
                        binding.tvError.text = "Error: ${response.code()} ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    binding.progress.isVisible = false
                    binding.swipeRefresh.isRefreshing = false
                    binding.tvError.text = "Network error: ${t.localizedMessage}"
                    binding.tvError.isVisible = true
                }
            })
    }
}
