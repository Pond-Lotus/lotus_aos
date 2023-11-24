package com.example.todo_android.di

import com.example.todo_android.dataresource.Category.CategoryRemoteDataResource
import com.example.todo_android.dataresource.Category.CategoryRemoteDataResourceImpl
import com.example.todo_android.dataresource.Todo.TodoRemoteDataResource
import com.example.todo_android.dataresource.Todo.TodoRemoteDataResourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindTodoDataSource(
        todoRemoteDataResourceImpl: TodoRemoteDataResourceImpl
    ): TodoRemoteDataResource

    @Singleton
    @Binds
    abstract fun bindCategoryDataSource(
        categoryRemoteDataResourceImpl: CategoryRemoteDataResourceImpl
    ): CategoryRemoteDataResource
}