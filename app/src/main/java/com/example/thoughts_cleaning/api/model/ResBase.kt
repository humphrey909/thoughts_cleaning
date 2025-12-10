package com.example.thoughts_cleaning.api.model

import com.example.thoughts_cleaning.common.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by SeoKang on 2021-05-21.
 */
open class ResBase(
    @SerializedName(Constants.KEY_RESULT)
    val result: String = "",

    @SerializedName(Constants.KEY_ERROR_CODE)
    val errorCode: String = "",

    @SerializedName(Constants.KEY_ERROR_MESSAGE)
    val errorMessage: String = ""
)


