package com.depi.whatnow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.depi.whatnow.databinding.ActivityNewsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NewsAdapter { article ->
            val url = article.url
            if (!url.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                Toast.makeText(this, "No article link found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.newsList.layoutManager = LinearLayoutManager(this)
        binding.newsList.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { loadNews() }

        loadNews()
    }

    private fun loadNews() {
        binding.progress.isVisible = true
        binding.tvError.isVisible = false

        val category = intent.getStringExtra("category") ?: "general"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NewsCallable::class.java)
        api.getNewsByCategory(category = category).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false

                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()!!.articles
                    if (articles.isNotEmpty()) {
                        adapter.submitList(articles)
                        binding.newsList.isVisible = true
                    } else {
                        binding.tvError.text = "No news found for this category"
                        binding.tvError.isVisible = true
                    }
                } else {
                    binding.tvError.text = "Error: ${response.code()} ${response.message()}"
                    binding.tvError.isVisible = true
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
