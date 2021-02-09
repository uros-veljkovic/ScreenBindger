package com.example.screenbindger.util.extensions

import com.example.screenbindger.db.remote.response.ErrorResponse
import com.example.screenbindger.db.remote.response.ValidateRequestTokenResponseFail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import kotlin.reflect.KClass

fun <T : Any> Response<T>.getErrorResponse(): ErrorResponse {
    val gson = Gson()
    val type = object : TypeToken<ErrorResponse>() {}.type
    val errorResponse: ErrorResponse =
        gson.fromJson(errorBody()!!.charStream(), type)

    return errorResponse
}
/*                val gson = Gson()
                val type = object : TypeToken<ValidateRequestTokenResponseFail>() {}.type
                val errorResponse: ValidateRequestTokenResponseFail? =
                    gson.fromJson(response.errorBody()!!.charStream(), type)
                val code = errorResponse!!.statusCode*/