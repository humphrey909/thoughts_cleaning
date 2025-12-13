package com.example.thoughts_cleaning.views.main.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.common.vm.MasilViewModel

class MainFragmentViewModel: MasilViewModel() {

    val _currentMainFlow: MutableLiveData<MainFlow> = MutableLiveData(MainFlow.COMMON)
    val currentMainFlow: LiveData<MainFlow> = _currentMainFlow

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    fun onClicked(){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
//        Log.d("currentMainFlow", "ENTER_GAME2: ${currentMainFlow.value}")


        _currentMainFlow.postValue(MainFlow.RECORD_PROBLEM)
    }

    fun onClickSetting(){
        _currentMainFlow.postValue(MainFlow.SETTING)
    }



//    enum class SubscribeState { COMMON, SUBSCRIBE, NO_SUBSCRIBE }

    enum class MainFlow {COMMON, ENTER_GAME, RECORD_PROBLEM, SETTING}
}