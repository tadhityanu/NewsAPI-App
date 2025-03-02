package com.example.alfagifttest.Api

import com.example.alfagifttest.Data.Response.NewsArticle.NewsArticleResponse
import com.example.alfagifttest.Data.Response.NewsSource.NewsSourceResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines/sources")
    suspend fun getNewsSources(
        @Query("category") category: String,
        @Query("language") language: String,
    ) : Response<NewsSourceResponse>

    @GET("everything")
    suspend fun getNewsArticle(
        @Query("q") query: String,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ) : Response<NewsArticleResponse>

}