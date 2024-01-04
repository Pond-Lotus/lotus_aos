package com.example.todo_android.service.Category

import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.CategoryResponse.UpdateCategoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CategoryService {

    @GET("/todo/name/priority/")
    fun requestReadCategory(
        @Header("Authorization") token: String
    ): Call<ReadCategoryResponse>

    @POST("/todo/name/priority2/")
    fun requestUpdateCategory(
        @Header("Authorization") token: String,
        @Body updateCategory: Map<Int, String>
    ): Call<UpdateCategoryResponse>
}