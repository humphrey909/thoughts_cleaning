package com.example.thoughts_cleaning.api.response

import com.example.thoughts_cleaning.api.model.MSSocialProfileVO
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.model.ResMainCommon
import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("idx")
    val idx: Int,

    @SerializedName("access_token")
    val accessToken: String, // 기존 session_key의 역할

    @SerializedName("refresh_token")
    val refreshToken: String, // ★ 새로 추가되어야 할 핵심 필드

    @SerializedName("access_token_expires_in")
    val accessTokenExpiresIn: Long // 토큰 만료 시간 (선택적)
): ResBase()