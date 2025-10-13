package com.depi.whatnow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        setSupportActionBar(binding.toolbar)
        binding.toolbar.overflowIcon?.setTint(ContextCompat.getColor(this,R.color.white))



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

        val pref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val country = pref.getString("country", "us") ?: "us"


        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NewsCallable::class.java)
        api.getNewsByCategory(category = category, country = country).enqueue(object : Callback<News> {
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings ->{

                startActivity(Intent(this, SettingActivity::class.java))
                return true
            }
            R.id.logOut ->{
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(this,LoginActivity::class.java))
                return true
            }
            R.id.favorites ->{

                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
