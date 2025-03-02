package com.example.alfagifttest.Page.Article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem
import com.example.alfagifttest.Repo.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ListArticleViewModel(private val repo: NewsRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    fun setDefaultQuery(query: String) {
        searchQuery.value = query
    }

    fun articlesFlow(source : String): Flow<PagingData<ArticlesItem>> {
        return searchQuery
            .flatMapLatest { query ->
                repo.getNewsArtticle(query, source).cachedIn(viewModelScope)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun searchArticles(query: String) {
        searchQuery.value = query
    }
}