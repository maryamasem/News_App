package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.depi.whatnow.databinding.ActivityCategoriesBinding

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.overflowIcon?.setTint(ContextCompat.getColor(this,R.color.white))

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

        binding.btnEntertainment.setOnClickListener {
            openCategory("entertainment")
        }

        binding.btnTech.setOnClickListener {
            openCategory("technology")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings ->{
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SettingActivity::class.java))
                return true
            }
            R.id.logOut ->{
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
               // startActivity(Intent(this,LoginActivity::class.java))
                return true
            }
            R.id.favorites ->{
                Toast.makeText(this, "favorites", Toast.LENGTH_SHORT).show()
               startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
