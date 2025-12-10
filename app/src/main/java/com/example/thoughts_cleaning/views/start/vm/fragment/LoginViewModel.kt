package com.example.thoughts_cleaning.views.start.vm.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.MainApplication
import com.example.thoughts_cleaning.api.RetrofitClient
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.views.start.LoginEvent
import kotlinx.coroutines.launch
import kr.dnx.ble.android.touchcare.api.request.SocialKakaoLoginRequestData
import kr.dnx.ble.android.touchcare.api.request.SocialNaverLoginRequestData

class LoginViewModel(mContext: Context): ViewModel()  {

//    protected val api: TouchCareApi = MainApplication.getClient()

    val _moveEvent = MutableLiveData<MoveEvent>()
    val moveEvent: LiveData<MoveEvent> get() = _moveEvent

    init {


    }

    fun onClick(event: LoginEvent) {
//        when(event) {
//            LoginEvent.LOGO_IMAGE -> if(isDebugMode()) return
//            LoginEvent.LOGIN_BUTTON -> checkParams()
//            else -> { }
//        }

        _moveEvent.postValue(MoveEvent.Login(event))
    }


    // Activity 또는 ViewModel 등의 클래스에서
//    fun fetchData() {
//        // 2. ApiService를 통해 정의된 함수 호출
//        RetrofitClient.apiService.getUser(userId = 1).enqueue(object : retrofit2.Callback<User> {
//
//            // 3. 응답을 성공적으로 받았을 때
//            override fun onResponse(call: retrofit2.Call<User>, response: retrofit2.Response<User>) {
//                if (response.isSuccessful) {
//                    val user: User? = response.body()
//                    // TODO: 받아온 user 데이터를 처리하는 로직
//                } else {
//                    // HTTP 상태 코드는 성공이지만 서버 내부 오류 등
//                    // TODO: response.code() 와 response.errorBody() 처리
//                }
//            }
//
//            // 4. 통신 자체에 실패했을 때 (네트워크 오류 등)
//            override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
//                // TODO: 오류 처리 로직
//                t.printStackTrace()
//            }
//        })
//    }

    //네이버 로그인 시도
//    fun startNaverLogin(socialLoginRequestData: SocialNaverLoginRequestData) = viewModelScope.launch(exceptionHandler) {
//
//        val response = api.startSocialNaverLogin(socialLoginRequestData)
//
//        response.call() {
//            onSuccess = {
////                Log.e(TAG, "getPaidProduct: $response")
////                Log.e(TAG, "getPaidProduct: ${it.nextType}")
////                Log.e(TAG, "getPaidProduct: ${it.profile}")
////                Log.e(TAG, "getPaidProduct: ${it.sessionKey}")
////                Log.e(TAG, "getPaidProduct: ${it.nextMessage}")
////                Log.e(TAG, "getPaidProduct: ${it.useUsertypeList}")
//
//                profileInfo = it.profile
//
//                if(it.nextType == "1") { //회원가입
//                    Prefs.sessionKey = it.sessionKey
//
//                    if(it.useUsertypeList.size != 0) {
//                        useUsertypeList = it.useUsertypeList
//                    }else{
//                        useUsertypeList.add(MSUserTypeVO("X"))
//                    }
//
//                    _moveEvent.postValue(MoveEvent.Login(LoginEvent.SOCIAL_REGISTER))
//                }else if(it.nextType == "2"){ //로그인
//                    if(it.profile?.loginId != null){
////                        Log.e(TAG, "startNaverLogin: ${it.profile?.loginId}")
////                        Log.e(TAG, "startNaverLogin: ${loginInfo.value}")
//
//                        val loginInfo = LoginInfo(it.profile.loginId, "1111")
//                        postUserToken(LoginSelectType.SOCIAL, ReqUserToken(loginInfo))
//                    }
//                }else{ //0 취소
//                    _moveEvent.postValue(MoveEvent.Login(LoginEvent.REGISTER_FAIL))
//                }
//            }
//            onFailure = { _moveEvent.postValue(MoveEvent.Login(LoginEvent.NETWORK_FAIL)) }
//            onNetWorkError = { _moveEvent.postValue(MoveEvent.Login(LoginEvent.NETWORK_FAIL)) }
//        }
//    }


    //카카오 로그인 진행
//    fun startKakaoLogin(socialKakaoLoginRequestData: SocialKakaoLoginRequestData) = viewModelScope.launch(exceptionHandler) {
//
//        val response = api.startSocialKakaoLogin(socialKakaoLoginRequestData)
//
//        response.call() {
//            onSuccess = {
//                Log.e(TAG, "getPaidProduct: $response")
//                Log.e(TAG, "getPaidProduct: ${it.nextType}")
//                Log.e(TAG, "getPaidProduct: ${it.profile}")
//                Log.e(TAG, "getPaidProduct: ${it.sessionKey}")
//                Log.e(TAG, "getPaidProduct: ${it.nextMessage}")
//                Log.e(TAG, "getPaidProduct: ${it.useUsertypeList}")
//
//                profileInfo = it.profile
//
//                if(it.nextType == "1") { //회원가입
//                    Prefs.sessionKey = it.sessionKey
//
//                    if(it.useUsertypeList.size != 0) {
//                        useUsertypeList = it.useUsertypeList
//                    }else{
//                        useUsertypeList.add(MSUserTypeVO("X"))
//                    }
//
//                    _moveEvent.postValue(MoveEvent.Login(LoginEvent.SOCIAL_REGISTER))
//                }else if(it.nextType == "2"){ //로그인
//                    if(it.profile?.loginId != null){
////                        Log.e(TAG, "startNaverLogin: ${it.profile?.loginId}")
////                        Log.e(TAG, "startNaverLogin: ${loginInfo.value}")
//
//                        val loginInfo = LoginInfo(it.profile.loginId, "1111")
//                        postUserToken(LoginSelectType.SOCIAL, ReqUserToken(loginInfo))
//                    }
//                }else{ //0 취소
//                    _moveEvent.postValue(MoveEvent.Login(LoginEvent.REGISTER_FAIL))
//                }
//            }
//            onFailure = { _moveEvent.postValue(MoveEvent.Login(LoginEvent.NETWORK_FAIL)) }
//            onNetWorkError = { _moveEvent.postValue(MoveEvent.Login(LoginEvent.NETWORK_FAIL)) }
//        }
//    }
}