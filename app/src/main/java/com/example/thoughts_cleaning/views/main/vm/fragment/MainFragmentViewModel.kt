package com.example.thoughts_cleaning.views.main.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainFragmentViewModel: ViewModel() {

    val _currentMainFlow: MutableLiveData<MainFlow> = MutableLiveData(MainFlow.COMMON)
    val currentMainFlow: LiveData<MainFlow> = _currentMainFlow


    fun onClicked(){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
//        Log.d("currentMainFlow", "ENTER_GAME2: ${currentMainFlow.value}")


        _currentMainFlow.postValue(MainFlow.ENTER_GAME)
    }



//    enum class SubscribeState { COMMON, SUBSCRIBE, NO_SUBSCRIBE }

    enum class MainFlow {COMMON, ENTER_GAME}
}