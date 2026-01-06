package com.example.thoughts_cleaning.views.main.vm.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.api.request.ThoughtSaveRequestData
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListDto
import com.example.thoughts_cleaning.api.response.ThoughtSaveResponseData
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import kotlinx.coroutines.launch

class MainFragmentViewModel: MasilViewModel() {

    val _currentMainFlow: MutableLiveData<MainFlow> = MutableLiveData(MainFlow.COMMON)
    val currentMainFlow: LiveData<MainFlow> = _currentMainFlow

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val fixDustDetail: MutableLiveData<String> = MutableLiveData("")

    var thoughtSaveResponseData: ThoughtSaveResponseData? = null

    //생각 저장 후 idx
    val _thoughtSaveIdx: MutableLiveData<Int> = MutableLiveData(0)
    val thoughtSaveIdx: LiveData<Int> = _thoughtSaveIdx


    //내 생각들 리스트
//    var thoughtListResponseData: ResThoughtOfUserListDto? = null
    var thoughtListResponseData: ResThoughtOfUserListDto? = null

    val _thoughtListSize: MutableLiveData<String> = MutableLiveData("")
    val thoughtListSize: LiveData<String> = _thoughtListSize


    fun onClicked(){
        _currentMainFlow.postValue(MainFlow.ENTER_GAME)
    }

    fun onClickSetting(){
        _currentMainFlow.postValue(MainFlow.SETTING)
    }

    fun saveThought() = viewModelScope.launch() {

        val response = api.thoughtsSave(ThoughtSaveRequestData(fixDustDetail.value))

        response.call() {
            onSuccess = {
                Log.d("saveThought", it.toString())
                thoughtSaveResponseData = it

                _thoughtSaveIdx.postValue(it.idx)
            }
        }
    }

    fun getListThought() = viewModelScope.launch() {

        val response = api.thoughtsOfUserListCount()

        response.call() {
            onSuccess = {
                Log.d("getListThought", it.toString())
//                thoughtListResponseData = it
                _thoughtListSize.postValue(it.thoughtsCount.toString())
            }
        }
    }


    enum class MainFlow {COMMON, ENTER_GAME, SETTING}
}