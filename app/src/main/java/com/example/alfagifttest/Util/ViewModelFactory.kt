package com.example.alfagifttest.Util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alfagifttest.Page.Article.ListArticleViewModel
import com.example.alfagifttest.Page.NewsSource.NewsSourceViewModel
import com.example.alfagifttest.Repo.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(NewsSourceViewModel::class.java) -> {
                NewsSourceViewModel(Injection.provideRepo(context)) as T
            }
            modelClass.isAssignableFrom(ListArticleViewModel::class.java) -> {
                ListArticleViewModel(Injection.provideRepo(context)) as T
            }
            else ->throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}