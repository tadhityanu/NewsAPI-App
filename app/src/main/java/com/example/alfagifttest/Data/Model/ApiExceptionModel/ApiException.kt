package com.example.alfagifttest.Data.Model.ApiExceptionModel

class ApiException(
    message: String,
    val code: Int
) : Exception(message)
