package com.example.thoughts_cleaning.api.model

import com.google.gson.annotations.SerializedName

data class DustFeelingItem(
    @SerializedName("IDX")
    var index: Int,

    @SerializedName("STATE")
    var state: Boolean,

    @SerializedName("DOCUMENT")
    val document: String

)
