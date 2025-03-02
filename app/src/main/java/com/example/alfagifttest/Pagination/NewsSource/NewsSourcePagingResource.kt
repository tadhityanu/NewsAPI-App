package com.example.alfagifttest.Pagination.NewsSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.alfagifttest.Api.ApiService
import com.example.alfagifttest.Data.Model.ApiExceptionModel.ApiException
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem

class NewsSourcePagingResource(private val apiService: ApiService, val category : String) : PagingSource<Int, SourcesItem>() {
    private var fullData: List<SourcesItem>? = null // Store full dataset in memory

    override fun getRefreshKey(state: PagingState<Int, SourcesItem>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SourcesItem> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize

            if (fullData == null) {
                val response = apiService.getNewsSources(category, "en")

                when(response.code()){
                    429 -> throw ApiException("Too many request, please update API Access", 429)
                    500 -> throw ApiException("Server error! Please try again later.", 500)
                    401 -> throw ApiException("Access denied through API Key failure", 401)
                    400 -> throw ApiException("Unexpected error: ${response.code()}", 400)
                }
                fullData = response.body()?.sources
            }

            val startIndex = (currentPage - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, fullData!!.size)

            val pageData = if (startIndex < fullData!!.size) {
                fullData!!.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            LoadResult.Page(
                data = pageData,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (endIndex < fullData!!.size) currentPage + 1 else null
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}