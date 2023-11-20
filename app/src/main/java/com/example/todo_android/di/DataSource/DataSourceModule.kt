package com.example.todo_android.di.DataSource

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
    ) : TodoRemoteDataResource
}