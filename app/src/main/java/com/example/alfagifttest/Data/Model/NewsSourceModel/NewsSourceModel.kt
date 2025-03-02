package com.example.alfagifttest.Data.Model.NewsSourceModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsSourceModel(
    val id : String,
    val name : String,
    val description : String,
    val url : String,
    val category : String,
) : Parcelable
