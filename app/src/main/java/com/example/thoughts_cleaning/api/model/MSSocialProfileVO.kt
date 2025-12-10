package com.example.thoughts_cleaning.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by humphrey on 2024/02/20.
 */
data class MSSocialProfileVO(
    @SerializedName("loginCompany")
    val loginCompany: String? = "",

    @SerializedName("loginId")
    val loginId: String? = "",

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("email")
    val email: String? = "",

    @SerializedName("gender")
    val gender: String? = "",

    @SerializedName("age")
    val age: String? = "",

    @SerializedName("birthdayType")
    val birthdayType: String? = "",

    @SerializedName("birthday")
    val birthday: String? = "",

    @SerializedName("countryNo")
    val countryNo: String? = "",

    @SerializedName("phoneNo")
    val phoneNo: String? = ""

) : Serializable {}
