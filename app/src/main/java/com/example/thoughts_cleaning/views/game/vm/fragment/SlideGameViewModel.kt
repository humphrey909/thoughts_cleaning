package com.example.thoughts_cleaning.views.game.vm.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListCustomDto
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListDto
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import kotlinx.coroutines.launch

class SlideGameViewModel: MasilViewModel() {
    var thoughtIdx = 0

    //내 생각들 리스트
//    var thoughtListResponseData: ResThoughtOfUserListDto? = null


    //ResThoughtOfUserListCustomDto

    val _thoughtListResponseData: MutableLiveData<ResThoughtOfUserListDto> = MutableLiveData(null)
    val thoughtListResponseData: LiveData<ResThoughtOfUserListDto> = _thoughtListResponseData


    fun getListThought() = viewModelScope.launch() {

        val response = api.thoughtsOfUserListCustom()

        response.call() {
            onSuccess = {
                Log.d("getListThought", it.toString())
//                thoughtListResponseData = it
                _thoughtListResponseData.postValue(it)
            }
        }
    }

}