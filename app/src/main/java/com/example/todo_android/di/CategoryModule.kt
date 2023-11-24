package com.example.todo_android.di

import com.example.todo_android.repository.Category.CategoryRepository
import com.example.todo_android.repository.Category.CategoryRepositoryImpl
import com.example.todo_android.repository.Todo.TodoRepository
import com.example.todo_android.repository.Todo.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryModule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}