package com.depi.whatnow

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.depi.whatnow.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var categorySelected: String = "general"
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categorySelected = intent.getStringExtra("category") ?: "general"

        setupRecycler()
        loadNews()

        binding.swipeRefresh.setOnRefreshListener {
            loadNews()
        }



    }

    private fun setupRecycler() {
        adapter = NewsAdapter { article ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)
        }
        binding.newsList.layoutManager = LinearLayoutManager(this)
        binding.newsList.adapter = adapter
    }

    private fun loadNews() {
        binding.progress.isVisible = true
        binding.tvError.isVisible = false

        val pref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val country = pref.getString("country", "us") ?: "us"

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

                    if (response.isSuccessful) {
                        val articles = response.body()?.articles ?: arrayListOf()
                        adapter.submitList(articles)
                    } else {
                        binding.tvError.text = "Failed to load: ${response.code()}"
                        binding.tvError.isVisible = true
                    }
                }
                override fun onFailure(call: Call<News>, t: Throwable) {
                    binding.progress.isVisible = false
                    binding.swipeRefresh.isRefreshing = false
                    binding.tvError.text = "Network error: ${t.message}"
                    binding.tvError.isVisible = true
                }
            })
    }
}
