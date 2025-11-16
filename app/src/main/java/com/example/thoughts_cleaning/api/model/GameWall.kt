package com.example.thoughts_cleaning.api.model

import com.google.gson.annotations.SerializedName

data class GameWall(
//    @SerializedName("POINT_RIGHT_TOP")
//    var wall: Wall
//    @SerializedName("REC_POINT")
//    var recPoint: RectF,

//    @SerializedName("COLOR")
//    var color: Paint,
    @SerializedName("LEFT")
    var left: Float,

    @SerializedName("TOP")
    var top: Float,

    @SerializedName("RIGHT")
    var right: Float,

    @SerializedName("BOTTOM")
    var bottom: Float,

    @SerializedName("COLOR")
    var color: Int
)
