package com.example.thoughts_cleaning.api.response

import com.example.thoughts_cleaning.api.model.MSSocialProfileVO
import com.example.thoughts_cleaning.api.model.ResMainCommon
import com.google.gson.annotations.SerializedName

data class ResMasilSocialLogin(
    @SerializedName("session_key")
    var sessionKey: String,

    @SerializedName("profile")
    val profile: MSSocialProfileVO?,

    @SerializedName("next_type")
    var nextType: String,

    @SerializedName("next_message")
    var nextMessage: String,

//    @SerializedName("use_usertype_list")
//    var useUsertypeList: ArrayList<MSUserTypeVO>,

    ): ResMainCommon()