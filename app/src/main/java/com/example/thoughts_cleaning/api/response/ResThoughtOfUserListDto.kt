package com.example.thoughts_cleaning.api.response

import android.os.Parcelable
import com.example.thoughts_cleaning.api.model.KindThoughtList
import com.example.thoughts_cleaning.api.model.MSSocialProfileVO
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.model.ResMainCommon
import com.example.thoughts_cleaning.api.model.ThoughtOfUserList
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResThoughtOfUserListDto(
    @SerializedName("thoughts_list")
    val thoughtsList: List<ThoughtOfUserList>
): ResBase()