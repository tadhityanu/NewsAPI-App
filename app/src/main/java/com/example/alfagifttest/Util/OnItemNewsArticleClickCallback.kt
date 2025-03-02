package com.example.alfagifttest.Util

import com.example.alfagifttest.Data.Model.NewsCategoryModel.NewsCategoryModel
import com.example.alfagifttest.Data.Response.NewsArticle.ArticlesItem
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem

interface OnItemNewsArticleClickCallback {

    fun itemNewsArticleClicked (data : ArticlesItem)

}