package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.api.model.DustKindItem
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.main.SettingEvent
import kotlinx.coroutines.launch

class SelectKindDustViewModel(mContext: Context): MasilViewModel() {

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val _currentFlow: MutableLiveData<TypeFlow> = MutableLiveData(TypeFlow.COMMON)
    val currentFlow: LiveData<TypeFlow> = _currentFlow

    val _dustKindListTotal: MutableLiveData<ArrayList<DustKindItem>> = MutableLiveData(null)
    val dustKindListTotal: LiveData<ArrayList<DustKindItem>> = _dustKindListTotal
    val dustKindList: ArrayList<DustKindItem> = ArrayList()

    val _fixDustKind: MutableLiveData<DustKindItem> = MutableLiveData(null)
    val fixDustKind: LiveData<DustKindItem> = _fixDustKind

    init {
//        _dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")
    }

    fun onClickedBack(){
        _currentFlow.postValue(TypeFlow.BACK)

    }

    fun onClickedForward(){
        _currentFlow.postValue(TypeFlow.NEXT_PAGE)
    }

    fun thoughtsKindList() = viewModelScope.launch() {

        val response = api.thoughtsKindList()

        response.call() {
            onSuccess = {
                for (item in it.kindThoughtList) {
                    dustKindList.add(DustKindItem(item.idx,false, item.name +System.lineSeparator()+ item.detailText))
                }
                _dustKindListTotal.postValue(dustKindList)
            }
        }
    }

    enum class TypeFlow {COMMON, NEXT_PAGE, BACK}
}