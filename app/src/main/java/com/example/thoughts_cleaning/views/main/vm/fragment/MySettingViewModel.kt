package com.example.thoughts_cleaning.views.main.vm.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.api.request.SocialKakaoLoginRequestData
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.*
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.RecordStageFragmentViewModel.RecordStageFlow
import kotlinx.coroutines.launch

class MySettingViewModel: MasilViewModel() {

    fun onClick(event: SettingEvent) {
        _moveEvent.postValue(Setting(event))
//        when (event) {
//            SettingEvent.COMMON -> {
//                _moveEvent.postValue(Setting(event))
//            }
//            SettingEvent.USER_INFO -> {
//                _moveEvent.postValue(Setting(event))
//            }
//            SettingEvent.LOGOUT -> {
//                _moveEvent.postValue(Setting(event))
//            }
//            SettingEvent.GO_LOGIN -> TODO()
//            SettingEvent.BACK -> {
//                _moveEvent.postValue(Setting(event))
//            }
//
//            SettingEvent.USERINFO -> {
//                _moveEvent.postValue(Setting(event))
//            }
//
//            SettingEvent.TERMS_AND_POLICIES -> {
//                _moveEvent.postValue(Setting(event))
//            }
//        }
    }

    fun logout(event: SettingEvent) = viewModelScope.launch() {

        val response = api.logout()

        response.call() {
            onSuccess = {
                Prefs.initUserInfo()

                _moveEvent.postValue(MoveEvent.Setting(event))
            }
        }
    }
}