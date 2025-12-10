package com.example.thoughts_cleaning.views.record_problem.vm.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.DustKindItem

class SelectKindDustViewModel(mContext: Context): ViewModel() {

    val _dustFairyMessageText: MutableLiveData<String> = MutableLiveData("")
    val dustFairyMessageText: LiveData<String> = _dustFairyMessageText

    val _currentFlow: MutableLiveData<TypeFlow> = MutableLiveData(TypeFlow.COMMON)
    val currentFlow: LiveData<TypeFlow> = _currentFlow

    val dustKindList: ArrayList<DustKindItem> = ArrayList()

    val _fixDustKind: MutableLiveData<DustKindItem> = MutableLiveData(null)
    val fixDustKind: LiveData<DustKindItem> = _fixDustKind

    init {
        dustKindList.add(DustKindItem(0,false, mContext.getString(R.string.dust_kind_thought1)))
        dustKindList.add(DustKindItem(1,false, mContext.getString(R.string.dust_kind_thought2)))
        dustKindList.add(DustKindItem(2,false,mContext.getString(R.string.dust_kind_thought3)))
        dustKindList.add(DustKindItem(3,false, mContext.getString(R.string.dust_kind_thought4)))
        dustKindList.add(DustKindItem(4,false, mContext.getString(R.string.dust_kind_thought4)))
        dustKindList.add(DustKindItem(5,false, mContext.getString(R.string.dust_kind_thought4)))
        dustKindList.add(DustKindItem(6,false, mContext.getString(R.string.dust_kind_thought4)))
        dustKindList.add(DustKindItem(7,false, mContext.getString(R.string.dust_kind_thought4)))
        dustKindList.add(DustKindItem(8,false, mContext.getString(R.string.dust_kind_thought4)))

        _dustFairyMessageText.postValue("오늘은 어떤 쓰레기를 버리고 싶으세요?")
    }

    fun onClickedBack(){
        _currentFlow.postValue(TypeFlow.BACK)

    }

    fun onClickedForward(){
        _currentFlow.postValue(TypeFlow.NEXT_PAGE)
    }

    enum class TypeFlow {COMMON, NEXT_PAGE, BACK}
}