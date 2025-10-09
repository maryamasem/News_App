package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.depi.whatnow.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun openCategory(category: String) {
            Toast.makeText(this, "Loading $category news...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NewsActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }

        binding.btnSports.setOnClickListener {
            openCategory("sports")
        }

        binding.btnHealth.setOnClickListener {
            openCategory("health")
        }

        binding.btnBusiness.setOnClickListener {
            openCategory("business")
        }

        binding.btnPolitics.setOnClickListener {
            openCategory("politics")
        }
    }
}
