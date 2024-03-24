package com.lotus.todo_android.repository.Category

import com.lotus.todo_android.common.APIResponse
import com.lotus.todo_android.dataresource.Category.CategoryRemoteDataResource
import com.lotus.todo_android.response.CategoryResponse.ReadCategoryResponse
import com.lotus.todo_android.response.CategoryResponse.UpdateCategoryResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val CategoryRemoteDataResource: CategoryRemoteDataResource
) : CategoryRepository {
    override suspend fun readTodoCategory(token: String): com.lotus.todo_android.common.APIResponse<ReadCategoryResponse> {

        // todo 생성 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response = CategoryRemoteDataResource.readTodoCategory(token).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }

    override suspend fun updateTodoCategory(
        token: String,
        category: Map<Int, String>
    ): com.lotus.todo_android.common.APIResponse<UpdateCategoryResponse> {

        // todo 생성 요청이 성공하면 Success에 데이터를 실어서 ,실패하면 Error에 message 리턴
        val response =
            CategoryRemoteDataResource.updateTodoCategory(token, category).clone().execute()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return com.lotus.todo_android.common.APIResponse.Success(result)
            }
        }
        return com.lotus.todo_android.common.APIResponse.Error(response.message())
    }
}