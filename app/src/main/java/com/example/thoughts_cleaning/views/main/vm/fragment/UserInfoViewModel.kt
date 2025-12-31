package com.example.thoughts_cleaning.views.main.vm.fragment

import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.MoveEvent.*
import com.example.thoughts_cleaning.base.MoveEvent.Setting
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.main.UserInfoEvent
import kotlinx.coroutines.launch

class UserInfoViewModel: MasilViewModel() {

    fun onClick(event: UserInfoEvent) {
        when (event) {
            UserInfoEvent.COMMON -> {
                _moveEvent.postValue(UserInfo(event))
            }
            UserInfoEvent.USER_DELETE -> {
                _moveEvent.postValue(UserInfo(event))
            }
            UserInfoEvent.BACK -> {
                _moveEvent.postValue(UserInfo(event))
            }
            UserInfoEvent.GO_LOGIN -> {
                _moveEvent.postValue(UserInfo(event))
            }
        }
    }

    fun withdraw(event: UserInfoEvent) = viewModelScope.launch() {

        val response = api.withdraw()

        response.call() {
            onSuccess = {
                Prefs.initUserInfo()

                _moveEvent.postValue(MoveEvent.UserInfo(event))
            }
        }
    }
}