package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustKindItem
import com.example.thoughts_cleaning.api.request.ThoughtSaveRequestData
import com.example.thoughts_cleaning.api.response.ThoughtSaveResponseData
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

    var kindThoughtIdx = 0 //전 페이지에서 받아온 생각 종류

    //생각 저장 후 idx
    val _thoughtSaveIdx: MutableLiveData<Int> = MutableLiveData(0)
    val thoughtSaveIdx: LiveData<Int> = _thoughtSaveIdx

    //ThoughtSaveResponseData
    var thoughtSaveResponseData: ThoughtSaveResponseData? = null

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
                Log.d("saveThought", it.toString())
                thoughtSaveResponseData = it

                _thoughtSaveIdx.postValue(it.idx)
            }
        }
    }

    enum class RecordStageFlow {COMMON, NEXT_PAGE, BACK}
}