package com.example.thoughts_cleaning.api.response

import com.example.thoughts_cleaning.api.model.MSSocialProfileVO
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.model.ResMainCommon
import com.google.gson.annotations.SerializedName

data class CheckTokenDto(
    @SerializedName("idx")
    val idx: Int,

    @SerializedName("is_valid")
    val isValid: Boolean
): ResBase()