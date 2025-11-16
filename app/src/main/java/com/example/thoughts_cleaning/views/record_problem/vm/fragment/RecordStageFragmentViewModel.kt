package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.MainApplication
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustFeelingItem
import com.example.thoughts_cleaning.api.model.DustKindItem

class RecordStageFragmentViewModel(mContext: Context): ViewModel() {

    val _currentMainFlow: MutableLiveData<RecordStageFlow> = MutableLiveData(RecordStageFlow.COMMON)
    val currentMainFlow: LiveData<RecordStageFlow> = _currentMainFlow

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val _backBtnStateText: MutableLiveData<Boolean> = MutableLiveData(false)
    val backBtnStateText: LiveData<Boolean> = _backBtnStateText

//    var _reviewOtherPeople = MutableLiveData<Int>()
//    val reviewOtherPeople: LiveData<Int> get() = _reviewOtherPeople

    val reviewOtherPeople: ArrayList<String> = ArrayList()
    val dustKindList: ArrayList<DustKindItem> = ArrayList()
    val dustFeelingList: ArrayList<DustFeelingItem> = ArrayList()


//    var fixDustKind: DustKindItem? = null

    val _fixDustKind: MutableLiveData<DustKindItem> = MutableLiveData(null)
    val fixDustKind: LiveData<DustKindItem> = _fixDustKind


    val fixDustOneWord: MutableLiveData<String> = MutableLiveData("")
    val fixDustDetail: MutableLiveData<String> = MutableLiveData("")

    init {
        reviewOtherPeople.add(mContext.getString(R.string.ex_other_thought1))
        reviewOtherPeople.add(mContext.getString(R.string.ex_other_thought2))
        reviewOtherPeople.add(mContext.getString(R.string.ex_other_thought3))

        dustKindList.add(DustKindItem(0,false, mContext.getString(R.string.dust_kind_thought1)))
        dustKindList.add(DustKindItem(1,false, mContext.getString(R.string.dust_kind_thought2)))
        dustKindList.add(DustKindItem(2,false,mContext.getString(R.string.dust_kind_thought3)))
        dustKindList.add(DustKindItem(3,false, mContext.getString(R.string.dust_kind_thought4)))



        dustFeelingList.add(DustFeelingItem(0,false, mContext.getString(R.string.dust_feeling_thought1)))
        dustFeelingList.add(DustFeelingItem(1,false, mContext.getString(R.string.dust_feeling_thought2)))
        dustFeelingList.add(DustFeelingItem(2,false,mContext.getString(R.string.dust_feeling_thought3)))
        dustFeelingList.add(DustFeelingItem(3,false, mContext.getString(R.string.dust_feeling_thought4)))
        dustFeelingList.add(DustFeelingItem(4,false, mContext.getString(R.string.dust_feeling_thought5)))
        dustFeelingList.add(DustFeelingItem(5,false, mContext.getString(R.string.dust_feeling_thought6)))
    }


    fun onClicked(type:RecordStageFlow){
//        Log.d("currentMainFlow", "ENTER_GAME2: ENTER_GAME")
//        Log.d("currentMainFlow", "ENTER_GAME2: ${currentMainFlow.value}")

        when (type) {
            RecordStageFlow.COMMON -> {
                _backBtnStateText.postValue(false)
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

    fun onClickedForward(){
        if(currentMainFlow.value == RecordStageFlow.STAGE_2){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_3)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_3){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_4)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_4){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_5)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_5){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_6)
        }else if(currentMainFlow.value == RecordStageFlow.STAGE_6){
            _currentMainFlow.postValue(RecordStageFlow.STAGE_7)
        }
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