package com.example.thoughts_cleaning.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // API 서버의 기본 URL (반드시 '/'로 끝나야 합니다)
//    private const val BASE_URL = "https://api.example.com/"
    private const val BASE_URL = "http://34.69.78.123:8910/"

    // HttpLoggingInterceptor를 추가하여 네트워크 통신 로그를 확인할 수 있습니다.
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 통신 내용 전체 로깅
        })
        .connectTimeout(10, TimeUnit.SECONDS) // 연결 시간 초과
        .readTimeout(10, TimeUnit.SECONDS)    // 읽기 시간 초과
        .writeTimeout(10, TimeUnit.SECONDS)   // 쓰기 시간 초과
        .build()

    // Retrofit 인스턴스 초기화 (Lazy initialization)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // JSON 응답을 Data Class로 자동 변환하기 위해 Gson Converter 사용
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // ApiService 인터페이스의 구현체를 얻는 메서드
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}