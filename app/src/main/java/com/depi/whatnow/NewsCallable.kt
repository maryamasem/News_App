package com.depi.whatnow

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {
    @GET("/v2/top-headlines")
    fun getNewsByCategory(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "9dfb9424b8ec4f76b0f285ef54411c05",
        @Query("pageSize") pageSize: Int = 30
    ): Call<News>
}
