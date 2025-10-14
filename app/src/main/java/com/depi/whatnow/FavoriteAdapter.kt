package com.depi.whatnow

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FavoriteAdapter(val activity: FavoriteActivity):

    RecyclerView.Adapter<FavoriteAdapter.MVH>() {
    var articles: ArrayList<Article> = kotlin.collections.ArrayList()

    class MVH(views: View) : RecyclerView.ViewHolder(views) {
        val card = views.findViewById<CardView>(R.id.Article_container2)
        val image = views.findViewById<ImageView>(R.id.aritcle_image2)
        val newTitle = views.findViewById<TextView>(R.id.title_tv2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.MVH {
        val view = activity.layoutInflater.inflate(R.layout.fav_list_item, parent, false)
        return MVH(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.MVH, position: Int) {
        Glide.with(activity)
            .load(articles[position].urlToImage)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000)) // some animation with scroll
            .into(holder.image)



        holder.newTitle.text = articles[position].title
        holder.card.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, articles[position].url.toUri())
            activity.startActivity(intent)
        }


    }
    override fun getItemCount() = articles.size

    fun removeItem(position: Int) {
        articles.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Article = articles[position]
    fun update(newList: ArrayList<Article>) {
        articles = newList
        notifyDataSetChanged()
    }
}