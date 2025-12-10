package kr.dnx.ble.android.touchcare.api.request

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by humphrey on 2024/05/30.
 */
data class SocialKakaoLoginRequestData(
    @SerializedName("access_token")
    val accessToken: String?,

    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("access_expires")
    val accessExpires: String?,

    @SerializedName("refresh_expires")
    val refreshExpires: String?,

    @SerializedName("kakao_user_id")
    val kakaoUserId: String?, // !!

    @SerializedName("profile")
    val profile: com.kakao.sdk.user.model.Account?,

//    @SerializedName(Constants.KEY_APP_DEVICE_ID)
//    var appId: String = TouchApplication.instance.getDeviceIdNew(),
//
//    @SerializedName(SyncStateContract.Constants.KEY_MODEL_NAME)
//    var modelName: String = Build.MODEL,
//
//    @SerializedName(Constants.KEY_OS_VERSION)
//    var phoneOSVersion: String = Build.VERSION.RELEASE,

    ): Serializable {}