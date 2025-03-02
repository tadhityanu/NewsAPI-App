package com.example.alfagifttest.Repo

import android.content.Context
import com.example.alfagifttest.Api.ApiConfig

object Injection {
    fun provideRepo(context: Context) : NewsRepository{
        val apiService = ApiConfig.getApiService()
        val repository = NewsRepository(apiService)
        return repository
    }
}