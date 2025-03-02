package com.example.alfagifttest.Data.Response.NewsSource

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NewsSourceResponse(

    @field:SerializedName("sources")
    val sources: List<SourcesItem>,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class SourcesItem(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)
