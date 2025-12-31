package com.example.thoughts_cleaning.views.game.vm.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.UserInfo
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.game.WindowCleanEvent
import com.example.thoughts_cleaning.views.main.UserInfoEvent

class WindowViewModel: MasilViewModel() {

//    val _currentMainFlow: MutableLiveData<WindowViewFlow> = MutableLiveData(WindowViewFlow.COMMON)
//    val currentMainFlow: LiveData<WindowViewFlow> = _currentMainFlow


    val _anxietyWriteListSize: MutableLiveData<Int> = MutableLiveData(0)
    val anxietyWriteListSize: LiveData<Int> = _anxietyWriteListSize

    fun onClicked(type:WindowCleanEvent){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
        Log.d("currentMainFlow", "ENTER_GAME2: ${type}")

        if(type == WindowCleanEvent.SOLUTION){
            _moveEvent.postValue(MoveEvent.WindowClean(type))
        }else if(type == WindowCleanEvent.WASHER){
            _moveEvent.postValue(MoveEvent.WindowClean(type))
        }
    }

//    fun onClicked2(){
//        Log.d("currentMainFlow", "ENTER_GAME222: ")
//    }

    //solution
    //washer
//    enum class WindowViewFlow {COMMON, NEXT_PAGE, QUIT_PAGE, SOLUTION, WASHER}
}