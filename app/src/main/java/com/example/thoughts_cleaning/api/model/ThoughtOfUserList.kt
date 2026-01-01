package com.example.thoughts_cleaning.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ThoughtOfUserList(
    @SerializedName("idx")
    var idx: Int,

    @SerializedName("uid")
    var uid: Int,

    @SerializedName("kindThoughtIdx")
    var kindThoughtIdx: Int,

    @SerializedName("contentThought")
    var contentThought: String
)
