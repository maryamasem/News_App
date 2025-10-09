package com.depi.whatnow

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {
    @GET("/v2/top-headlines")
    fun getNewsByCategory(
        @Query("country") country: String = "us",
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "c25de7ffd53746339ed77c20497935de",
        @Query("pageSize") pageSize: Int = 30
    ): Call<News>
}
