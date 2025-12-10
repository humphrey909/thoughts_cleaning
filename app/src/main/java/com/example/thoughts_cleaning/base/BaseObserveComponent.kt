package com.example.thoughts_cleaning.base

import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.common.ErrorCodeApp

interface BaseObserveComponent {
    val customAppEvent: Observer<ErrorCodeApp?>?
    val defaultAppEvent: Observer<ErrorCodeApp?>
    val customServerEvent: Observer<Exception>?
    val defaultServerEvent: Observer<Exception>

    fun setObserveLiveData()
}