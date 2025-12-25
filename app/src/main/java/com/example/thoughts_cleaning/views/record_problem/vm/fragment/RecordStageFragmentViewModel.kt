package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustKindItem
import com.example.thoughts_cleaning.api.request.ThoughtSaveRequestData
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import kotlinx.coroutines.launch

class RecordStageFragmentViewModel(mContext: Context): MasilViewModel() {

    val _currentFlow: MutableLiveData<RecordStageFlow> = MutableLiveData(RecordStageFlow.COMMON)
    val currentFlow: LiveData<RecordStageFlow> = _currentFlow

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val _backBtnStateText: MutableLiveData<Boolean> = MutableLiveData(false)
    val backBtnStateText: LiveData<Boolean> = _backBtnStateText

    val fixDustDetail: MutableLiveData<String> = MutableLiveData("")

    var kindThoughtIdx = 0

    fun onClicked(type:RecordStageFlow){
        _backBtnStateText.postValue(false)
    }

    fun onClickedForward(){
        _currentFlow.postValue(RecordStageFlow.NEXT_PAGE)
    }


    fun onClickedBack(){
        _currentFlow.postValue(RecordStageFlow.BACK)
    }

    fun saveThought() = viewModelScope.launch() {

        val response = api.thoughtsSave(ThoughtSaveRequestData(kindThoughtIdx, fixDustDetail.value))

        response.call() {
            onSuccess = {

//                for (item in it.kindThoughtList) {
//                    dustKindList.add(DustKindItem(item.idx,false, item.name +System.lineSeparator()+ item.detailText))
//                }
//                _dustKindListTotal.postValue(dustKindList)
            }
        }
    }

    enum class RecordStageFlow {COMMON, NEXT_PAGE, BACK}
}