package com.example.todo_android.repository.Category

import com.example.todo_android.common.APIResponse
import com.example.todo_android.dataresource.Category.CategoryRemoteDataResource
import com.example.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.response.CategoryResponse.UpdateCategoryResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val CategoryRemoteDataResource: CategoryRemoteDataResource
) : CategoryRepository {
    override suspend fun readTodoCategory(token: String): APIResponse<ReadCategoryResponse> {

        // todo 생성 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = CategoryRemoteDataResource.readTodoCategory(token).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.message())
    }

    override suspend fun updateTodoCategory(
        token: String,
        category: Map<Int, String>
    ): APIResponse<UpdateCategoryResponse> {

        // todo 생성 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response =
            CategoryRemoteDataResource.updateTodoCategory(token, category).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return APIResponse.Success(result)
            }
        }
        return APIResponse.Error(response.message())
    }
}