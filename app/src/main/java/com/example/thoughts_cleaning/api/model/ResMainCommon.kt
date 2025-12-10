package com.example.thoughts_cleaning.api.model

import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.common.util.getVersionName
import com.google.gson.annotations.SerializedName
import kotlin.toString
import com.example.thoughts_cleaning.MainApplication.Companion as app

open class ResMainCommon(
    @SerializedName(Constants.KEY_USER_NAME)
    open val userName: String = "",

    @SerializedName(Constants.KEY_POINT)
    open var point: Int = 0,

    @SerializedName(Constants.KEY_LAST_APP_VERSION)
    open val lastAppVersion: String = app.instance.getVersionName().toString(),

    @SerializedName(Constants.KEY_TOTAL_STEP)
    open val totalStep: Int = 0,

    @SerializedName(Constants.KEY_IS_OLD_APP)
    open val isOldApp: Boolean = false,

    @SerializedName(Constants.KEY_NEED_HOME_GPS)
    open val needHomeGps: Boolean = false,

    @SerializedName(Constants.KEY_STEP_POINT_BOX)
    open val stepPointBox: Int = 0,

    @SerializedName(Constants.KEY_FOLLOWER_COUNT)
    open val followerCount: Int = 0,

    ) : ResBase() {

    fun getStepWithFormat(): String = "${totalStep}걸음"

    fun isShowingPointBox() : Boolean = stepPointBox > 0

    fun isRequestFollower(): Boolean = followerCount > 0

//    fun cleanUpData() {
//        when(this) {
//            is ResMasilRadio -> {
//                this.channels = this.channels.subList(1, this.channels.size)
//            }
//        }
//    }
}
