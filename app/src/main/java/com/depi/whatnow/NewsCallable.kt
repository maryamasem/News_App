package com.depi.whatnow

import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {

    @GET("/v2/top-headlines?country=us&category=general&apiKey=c25de7ffd53746339ed77c20497935de&pageSize=30")
    fun getNews(): Call<News>
}