package com.example.thoughts_cleaning.api.model

import com.google.gson.annotations.SerializedName

data class KindThoughtList(
    @SerializedName("idx")
    var idx: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("detailText")
    var detailText: String,

)
