package com.depi.whatnow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.depi.whatnow.databinding.ItemNewsBinding

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

            // تحميل الصورة باستخدام Glide
            Glide.with(newsImage.context)
                .load(article.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(newsImage)

            root.setOnClickListener {
                onClick(article)
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
