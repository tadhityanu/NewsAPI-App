package com.example.alfagifttest.Util

import com.example.alfagifttest.Data.Model.NewsCategoryModel.NewsCategoryModel
import com.example.alfagifttest.Data.Response.NewsSource.SourcesItem

interface OnItemNewsSourceClickCallback {

    fun itemNewsSourceClicked (data : SourcesItem)

}