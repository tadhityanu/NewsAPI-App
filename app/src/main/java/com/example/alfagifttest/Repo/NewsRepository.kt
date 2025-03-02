package com.example.alfagifttest.Repo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alfagifttest.Api.ApiConfig
import com.example.alfagifttest.Api.ApiService
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem
import com.example.alfagifttest.Data.Response.NewsSource.NewsSourceResponse
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem
import com.example.alfagifttest.Pagination.NewsArticle.NewsArticlePagingResource
import com.example.alfagifttest.Pagination.NewsSource.NewsSourcePagingResource
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository(private val apiService: ApiService) {

    fun getNewsSource(category: String): Flow<PagingData<SourcesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,  // Define number of items per page
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsSourcePagingResource(apiService, category) }
        ).flow
    }

    fun getNewsArtticle(query : String, source: String): Flow<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,  // Define number of items per page
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsArticlePagingResource(apiService, query, source) }
        ).flow
    }

}