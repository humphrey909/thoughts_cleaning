package com.example.thoughts_cleaning.api.model

import com.google.gson.annotations.SerializedName

data class CleanStateBtnItem(
    @SerializedName("COUNT")
    var count: Int,

    @SerializedName("IMG1")
    var img1: Int,

    @SerializedName("IMG2")
    val img2: Int,

    @SerializedName("IMG3")
    val img3: Int

)
