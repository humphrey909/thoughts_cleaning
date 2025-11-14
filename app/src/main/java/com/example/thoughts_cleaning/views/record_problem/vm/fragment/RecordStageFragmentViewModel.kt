package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordStageFragmentViewModel: ViewModel() {

    val _currentMainFlow: MutableLiveData<RecordStageFlow> = MutableLiveData(RecordStageFlow.COMMON)
    val currentMainFlow: LiveData<RecordStageFlow> = _currentMainFlow

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val _backBtnStateText: MutableLiveData<Boolean> = MutableLiveData(false)
    val backBtnStateText: LiveData<Boolean> = _backBtnStateText

    fun onClicked(type:RecordStageFlow){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
//        Log.d("currentMainFlow", "ENTER_GAME2: ${currentMainFlow.value}")

        when (type) {
            RecordStageFlow.COMMON -> {
                _backBtnStateText.postValue(false)
//                _currentMainFlow.postValue(RecordStageFlow.STAGE_1)
            }
            RecordStageFlow.STAGE_1 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_1)

            }
            RecordStageFlow.STAGE_2 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_2)

            }

            RecordStageFlow.STAGE_3 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_3)

            }
            RecordStageFlow.STAGE_4 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_4)

            }
            RecordStageFlow.STAGE_5 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_5)

            }
            RecordStageFlow.STAGE_6 -> {
                _backBtnStateText.postValue(true)
                _currentMainFlow.postValue(RecordStageFlow.STAGE_6)

            }
            RecordStageFlow.STAGE_7 -> {
                _backBtnStateText.postValue(true)
            }
        }

//        _currentMainFlow.postValue(RecordStageFlow.STAGE_1)
    }


    fun onClickedBack(){
        if(currentMainFlow.value == RecordStageFlow.STAGE_1){
            _currentMainFlow.postValue(RecordStageFlow.COMMON)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_2){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_1)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_3){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_2)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_4){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_3)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_5){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_4)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_6){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_5)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_7){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_6)
        }
    }



//    enum class SubscribeState { COMMON, SUBSCRIBE, NO_SUBSCRIBE }

    enum class RecordStageFlow {COMMON, STAGE_1, STAGE_2, STAGE_3, STAGE_4, STAGE_5, STAGE_6, STAGE_7}
}