package com.depi.whatnow

data class News(
    val articles: ArrayList<Article>
)

data class Article(
    val id :String= "",
    val title: String="",
    val url: String="",
    val urlToImage: String?="",
    var isFavorite: Boolean = false
)
