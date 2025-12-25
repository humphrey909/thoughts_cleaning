package com.example.thoughts_cleaning.api.request

import android.os.Build
import android.provider.SyncStateContract
import com.example.thoughts_cleaning.MainApplication
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.common.util.getDeviceIdNew
import com.google.gson.annotations.SerializedName
import com.kakao.sdk.user.model.Account

import java.io.Serializable

/**
 * Created by humphrey on 2024/05/30.
 */
data class GameGaugeSaveRequestData(
    @SerializedName("thought_idx")
    val thoughtIdx: Int?,

    @SerializedName("space_type")
    val spaceType: String?,

    @SerializedName("components_type")
    val componentsType: String?,

    @SerializedName("clean_gauge")
    val cleanGauge: Float?

    ): Serializable {}