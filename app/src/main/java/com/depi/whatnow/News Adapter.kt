package com.depi.whatnow

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.depi.whatnow.databinding.ItemNewsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NewsAdapter(
    private val onClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val articles = ArrayList<Article>()

    // ViewHolder
    inner class NewsViewHolder(val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        with(holder.binding) {
            newsTitle.text = article.title


            Glide.with(newsImage.context)
                .load(article.urlToImage)
                .error(R.drawable.broken_image)
                .transition(DrawableTransitionOptions.withCrossFade(1000)) // some animation with scroll
                .centerCrop()
//                .placeholder(R.drawable.placeholder)
                .into(newsImage)

            if (article.isFavorite) {
                favFab.setImageResource(R.drawable.favorite2)
            } else {
                favFab.setImageResource(R.drawable.baseline_favorite_border_24)
            }

            root.setOnClickListener {
                onClick(article)
            }

            holder.binding.favFab.setOnClickListener {

                if (!article.isFavorite) {
                    Firebase.firestore.collection("Article")
                        .add(articles[position])
                        .addOnSuccessListener {
                            it.update("id", it.id).addOnSuccessListener {
                                Log.d("success", "adding success: ")
                                article.isFavorite = true
                                holder.binding.favFab.setImageResource(R.drawable.favorite2)
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Added to favorites ❤️",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

            }

        }
    }

    override fun getItemCount(): Int = articles.size

    fun submitList(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
}
