package com.depi.whatnow

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.depi.whatnow.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    //https://newsapi.org
    //test github
   private lateinit var binding: ActivityMainBinding
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
        loadNews()

        binding.swipeRefresh.setOnRefreshListener { loadNews() }
    }

    private fun loadNews () {
        // make this in viewModel
        val reto = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val c = reto.create(NewsCallable::class.java)
        c.getNews().enqueue(object : Callback<News>{
            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                val news = response.body()
                val articles = news?.articles!!
                //Log.d("trace", "Article: $articles")
                showNews(articles)
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false

            }

            override fun onFailure(
                call: Call<News?>,
                t: Throwable
            ) {
                //Log.d("trace", "Error: ${t.message}")
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false



            }
        })
    }

    private fun showNews (articles: ArrayList<Article>){
        val adapter = NewsAdapter(this, articles)
        binding.newsList.adapter = adapter
    }
}