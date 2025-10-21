package com.example.thoughts_cleaning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel(){

    private val _characterImageResId = MutableLiveData<Int>(R.drawable.character_default)
    val characterImages: LiveData<Int> = _characterImageResId


    fun resetStopwatch() {
//        _isRunning.value = false
//        _timeInMillis.value = 0L
    }
}