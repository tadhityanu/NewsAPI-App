package com.example.alfagifttest.Page.NewsSource

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.alfagifttest.Api.ApiConfig
import com.example.alfagifttest.Data.Response.NewsSource.NewsSourceResponse
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem
import com.example.alfagifttest.Repo.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsSourceViewModel(private val repo: NewsRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSourceList(category: String): Flow<PagingData<SourcesItem>> {
        return repo.getNewsSource(category)
            .cachedIn(viewModelScope)
            .combine(searchQuery) { pagingData, query ->
                pagingData.filter {
                    it.name?.contains(query, ignoreCase = true) == true // Search by name
                }
            }
    }

}