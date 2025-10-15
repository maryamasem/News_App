package com.depi.whatnow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.depi.whatnow.databinding.ActivityFavoriteBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    val articles = ArrayList<Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.overflowIcon?.setTint(ContextCompat.getColor(this,R.color.white))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = FavoriteAdapter(this)
        binding.recycler2.adapter = adapter
        getArticles(adapter)

        val swipeToDeleteCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedArticle = adapter.getItem(position)

                // delete from firebase
                deleteArticleWithConfirmation(deletedArticle, position, adapter)

            }

        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recycler2)

    }

    private fun deleteArticleWithConfirmation(
        article: Article,
        position: Int,
        adapter: FavoriteAdapter
    ) {
        AlertDialog.Builder(this)
            .setTitle("Delete Article")
            .setMessage("Are you sure you want to delete this article?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()

                val db = Firebase.firestore
                db.collection("Article")
                    .whereEqualTo("id", article.id)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("Article").document(document.id).delete()
                        }

                        adapter.removeItem(position)

                        if (adapter.itemCount == 0) {
                            binding.recycler2.visibility = View.GONE
                            binding.noDataTv.visibility = View.VISIBLE
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(position)
            }
            .setCancelable(false)
            .show()
    }

    private fun getArticles(adapter: FavoriteAdapter) {
        binding.progress2.isVisible = true
        binding.noDataTv.visibility = View.GONE
        binding.recycler2.visibility = View.GONE

        Firebase.firestore.collection("Article")
            .get()
            .addOnSuccessListener { result ->
                articles.clear() // clear old data first
                for (document in result) {
                    val article = document.toObject(Article::class.java)
                    articles.add(article)
                }

                binding.progress2.isVisible = false

                if (articles.isEmpty()) {
                    // if no data
                    binding.recycler2.visibility = View.GONE
                    binding.noDataTv.visibility = View.VISIBLE
                } else {
                    binding.noDataTv.visibility = View.GONE
                    binding.recycler2.visibility = View.VISIBLE
                    adapter.update(articles)
                }
            }
            .addOnFailureListener { exception ->
                binding.progress2.isVisible = false
                binding.recycler2.visibility = View.GONE
                binding.noDataTv.visibility = View.VISIBLE
                Toast.makeText(this, "Error loading data: ${exception.message}", Toast.LENGTH_LONG)
                    .show()
                //Log.e("Firestore", "Error getting documents", exception)
            }
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
                Firebase.auth.signOut()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,LoginActivity::class.java))
                finishAffinity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}