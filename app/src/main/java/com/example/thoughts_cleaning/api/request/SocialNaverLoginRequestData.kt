package kr.dnx.ble.android.touchcare.api.request

import android.os.Build
import android.provider.SyncStateContract
import com.example.thoughts_cleaning.MainApplication
import com.google.gson.annotations.SerializedName
import com.navercorp.nid.profile.data.NidProfile
import java.io.Serializable

/**
 * Created by humphrey on 2024/05/30.
 */
data class SocialNaverLoginRequestData(
    @SerializedName("access_token")
    val accessToken:String?,

    @SerializedName("refresh_token")
    val refreshToken:String?,

    @SerializedName("expires")
    val expires:Long?,

    //사용안해서 뺏음
//    @SerializedName("type")
//    val type: String?,
//
//    @SerializedName("state")
//    val state: String?,

    @SerializedName("profile")
    val profile: NidProfile?,

//    @SerializedName(SyncStateContract.Constants.KEY_APP_DEVICE_ID)
//    var appId: String = MainApplication.instance.getDeviceIdNew(),
//
//    @SerializedName(SyncStateContract.Constants.KEY_MODEL_NAME)
//    var modelName: String = Build.MODEL,
//
//    @SerializedName(SyncStateContract.Constants.KEY_OS_VERSION)
//    var phoneOSVersion: String = Build.VERSION.RELEASE,

    ): Serializable {}