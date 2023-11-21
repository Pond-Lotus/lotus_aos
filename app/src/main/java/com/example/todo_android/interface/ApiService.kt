package com.example.todo_android.`interface`

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ApiService {

    companion object {
        private const val baseURL = "https://team-lotus.kr/"

        fun create() : ApiService {

            // 로그 메세지의 포맷을 설정
            val logger = HttpLoggingInterceptor().apply {

                // 로그 레벨: BODY
                level = HttpLoggingInterceptor.Level.BODY
            }

            // OkHttpClient: HTTP 통신의 결과를 로그 메세지로 출력해준다
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

}