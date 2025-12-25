package com.example.thoughts_cleaning.api.response

import com.example.thoughts_cleaning.api.model.KindThoughtList
import com.example.thoughts_cleaning.api.model.MSSocialProfileVO
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.api.model.ResMainCommon
import com.google.gson.annotations.SerializedName

data class ResKindThoughtListDto(
    @SerializedName("kind_thought_list")
    val kindThoughtList: List<KindThoughtList>
): ResBase()