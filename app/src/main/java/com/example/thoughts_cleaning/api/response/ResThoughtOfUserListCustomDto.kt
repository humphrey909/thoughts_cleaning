package com.example.thoughts_cleaning.api.response

import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.model.ThoughtOfUserCustomList
import com.google.gson.annotations.SerializedName

data class ResThoughtOfUserListCustomDto(
    @SerializedName("thoughts_list_custom")
    val thoughtsListCustom: List<ThoughtOfUserCustomList> = emptyList()
): ResBase()