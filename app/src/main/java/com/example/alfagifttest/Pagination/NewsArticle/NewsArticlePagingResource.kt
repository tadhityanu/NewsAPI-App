package com.example.alfagifttest.Pagination.NewsArticle

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfagifttest.Api.ApiService
import com.example.alfagifttest.Data.Model.ApiExceptionModel.ApiException
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem

class NewsArticlePagingResource(private val apiService: ApiService, val query : String, val source : String) :
    PagingSource<Int, ArticlesItem>() {
    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getNewsArticle(query, source, position, params.loadSize)
            val responseData = response.body()?.articles ?: emptyList()

            when(response.code()){
                429 -> throw ApiException("Too many request, please update API Access", 429)
                500 -> throw ApiException("Server error! Please try again later.", 500)
                401 -> throw ApiException("Access denied through API Key failure", 401)
                400 -> throw ApiException("Unexpected error: ${response.code()}", 400)
            }
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}