package com.lotus.todo_android.request.CategoryRequest

import com.lotus.todo_android.response.CategoryResponse.ReadCategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ReadCategoryRequest {
    @GET("/todo/name/priority/")
    fun requestReadCategory(
        @Header("Authorization") token: String
    ): Call<ReadCategoryResponse>
}