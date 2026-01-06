package com.example.thoughts_cleaning.api

import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.request.CheckTokenRequestData
import com.example.thoughts_cleaning.api.request.GameGaugeSaveRequestData
import com.example.thoughts_cleaning.api.request.RefreshTokenRequestData
import com.example.thoughts_cleaning.api.request.SocialKakaoLoginRequestData
import com.example.thoughts_cleaning.api.request.ThoughtSaveRequestData
import com.example.thoughts_cleaning.api.response.CheckTokenDto
import com.example.thoughts_cleaning.api.response.ResKindThoughtListDto
import com.example.thoughts_cleaning.api.response.ResMasilSocialLogin
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserCountDto
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListCustomDto
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListDto
import com.example.thoughts_cleaning.api.response.ThoughtSaveResponseData
import com.example.thoughts_cleaning.api.response.TokenDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ServerNetworkApi {
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


    /**
     * Created by Humphrey on
     * 카카오 로그인 진행
     * api/users/login
     */
    @POST("/api/users/social/kakao")
    fun startSocialKakaoLogin(
        @Body req: SocialKakaoLoginRequestData
    ): Call<TokenDto>

    /**
     * Created by Humphrey on
     * accessToken 데이터 만료 여부 체크
     * api/users/check-token
     */
    @POST("/api/users/check-token")
    fun startCheckToken(
    ): Call<CheckTokenDto>

    /**
     * Created by Humphrey on
     * accessToken 데이터 만료 여부 체크
     * api/users/refresh
     */
    @POST("/api/users/refresh")
    fun startNewToken(
        @Body req: RefreshTokenRequestData
    ): Call<TokenDto>

    /**
     * Created by Humphrey on
     * accessToken 데이터 만료 여부 체크
     * api/users/refresh
     */
    @POST("/api/users/logout")
    fun logout(
    ): Call<ResBase>

    /**
     * Created by Humphrey on
     * accessToken 데이터 만료 여부 체크
     * api/users/refresh
     */
    @DELETE("/api/users/withdraw")
    fun withdraw(
    ): Call<ResBase>



    /**
     * Created by Humphrey on
     * 생각 저장
     * api/thoughts/save
     */
    @POST("/api/thoughts/save")
    fun thoughtsSave(
        @Body req: ThoughtSaveRequestData
    ): Call<ThoughtSaveResponseData>

    /**
     * Created by Humphrey on
     * 생각 종류 조회
     * api/thoughts/save
     */
    @GET("/api/kind-thought/list")
    fun thoughtsKindList(
    ): Call<ResKindThoughtListDto>

    /**
     * Created by Humphrey on
     * 생각 종료 조회 횟수
     * api/thoughts/list_count
     */
    @GET("/api/thoughts/list_count")
    fun thoughtsOfUserListCount(
    ): Call<ResThoughtOfUserCountDto>

    /**
     * Created by Humphrey on
     * 생각 종료 조회 리스트 custom
     * api/thoughts/list_custom
     */
    @GET("/api/thoughts/list_custom")
    fun thoughtsOfUserListCustom(
    ): Call<ResThoughtOfUserListCustomDto>

    /**
     * Created by Humphrey on
     * 생각 저장
     * api/thoughts/save
     */
    @POST("/api/cleaning-gauge/save")
    fun cleaningGaugeSave(
        @Body req: GameGaugeSaveRequestData
    ): Call<ResBase>
}