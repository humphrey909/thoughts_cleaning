package com.example.thoughts_cleaning.views.game.vm.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PourThoughtViewModel: ViewModel() {

    val _currentMainFlow: MutableLiveData<PourThoughtViewFlow> = MutableLiveData(PourThoughtViewFlow.COMMON)
    val currentMainFlow: LiveData<PourThoughtViewFlow> = _currentMainFlow


    fun onClicked(type:String){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
        Log.d("currentMainFlow", "ENTER_GAME2: ${type}")

//        if(){
//
//        }
//
        _currentMainFlow.postValue(PourThoughtViewFlow.QUIT_PAGE)
    }

//    fun onClicked2(){
//        Log.d("currentMainFlow", "ENTER_GAME222: ")
//    }

    enum class PourThoughtViewFlow {COMMON, NEXT_PAGE, QUIT_PAGE}
}