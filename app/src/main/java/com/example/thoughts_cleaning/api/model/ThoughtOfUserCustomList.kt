package com.example.thoughts_cleaning.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ThoughtOfUserCustomList(
    @SerializedName("idx")
    var idx: Int,

    @SerializedName("kind")
    var kind: String,

    @SerializedName("sizeType")
    var sizeType: String,

    @SerializedName("count")
    var count: Int
)
