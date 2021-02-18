package com.example.screenbindger.util.extensions

import com.example.screenbindger.db.remote.response.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

fun <T : Any> Response<T>.getErrorResponse(): ErrorResponse {
    val gson = Gson()
    val type = object : TypeToken<ErrorResponse>() {}.type
    val errorResponse: ErrorResponse =
        gson.fromJson(errorBody()!!.charStream(), type)

    return errorResponse
}