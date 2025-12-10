package com.example.thoughts_cleaning.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // GET 요청 예시
    // Endpoint: BASE_URL/users/{userId}
//    @GET("users/{userId}")
//    fun getUser(
//        @Path("userId") userId: Int // URL 경로에 포함될 변수
//    ): Call<User> // User는 응답 JSON을 매핑할 데이터 클래스
//
//    // 다른 GET 요청 예시
//    // Endpoint: BASE_URL/posts?type=notice
//    @GET("posts")
//    fun getPosts(
//        @Query("type") type: String // 쿼리 파라미터
//    ): Call<List<Post>> // Post는 응답 JSON을 매핑할 데이터 클래스 리스트

    // POST, PUT, DELETE 등 다른 메서드도 @POST, @PUT, @DELETE 등으로 정의 가능
}