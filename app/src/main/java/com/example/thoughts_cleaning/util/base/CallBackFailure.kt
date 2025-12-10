package com.example.thoughts_cleaning.util.base

import com.example.thoughts_cleaning.api.model.ResBase


/**
 *
 */
interface CallBackFailure {
    fun onFailureInvalidSessionKey(errorMessage: String, target: Class<*>)
    fun onFailureNeedAppUpdate()

    fun onFailureResBase(error: ResBase)
}