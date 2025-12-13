package com.example.thoughts_cleaning.views.splash.vm

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.api.request.RefreshTokenRequestData
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.base.SplashEvent
import com.example.thoughts_cleaning.common.extension.call
import com.example.thoughts_cleaning.common.util.getVersionName
import com.example.thoughts_cleaning.common.vm.MasilViewModel
import com.example.thoughts_cleaning.views.start.LoginEvent
import kotlinx.coroutines.launch
import kotlin.text.isNotEmpty
import kotlin.toString
import com.example.thoughts_cleaning.MainApplication.Companion as app

class SplashViewModel : MasilViewModel() {

    val version: String = app.instance.getVersionName().toString()

    private var utmSource = ""
    private var utmMedium = ""
    private var utmCampaign = ""
    private var utmContent = ""
    private var utmTerm = ""

//    private lateinit var referrerClient: InstallReferrerClient

    fun checkParam() = viewModelScope.launch {
        var timerCount = 5

//        while (timerCount > 0) {
//            delay(1000)
//            timerCount = timerCount.minus(1)
//            if (timerCount > 3) continue
//
//            if (Prefs.fcmToken.isEmpty()) {
//                if (timerCount > 0) continue
//
//                _showAppEvent.postValue(null)
//            } else {
//                if (Prefs.sessionKey.isNotEmpty()) {
//                    checkUserType()
//                } else {
//                    _moveEvent.postValue(MoveEvent.Splash(SplashEvent.LOGIN))
//                }
//                break
//            }
//        }

        if(Prefs.accessToken?.isNotEmpty() == true) {
            //체크 진행
            checkUserType()
        }else{
            //로그인 페이지 이동
            _moveEvent.postValue(MoveEvent.Splash(SplashEvent.LOGIN))
        }
    }

    private fun checkUserType() {

//        accessToken 여부 체크를 위한 서버 요청
        //
        startCheckToken()

        
//        when (Prefs.userType) {
//            UserType.WEARER -> postUserOwnerLogin()
//
//            UserType.GUARDIAN,
//            UserType.CARE_GIVER,
//            UserType.ADMIN -> postUserGuardianLogin()
//
////            UserType.GUARDIAN,
//            UserType.NONE -> _moveEvent.postValue(MoveEvent.Splash(SplashEvent.LOGIN))
//        }
    }

    private fun startCheckToken() = viewModelScope.launch(exceptionHandler) {
//        val result = callApi { api.startCheckToken(CheckTokenRequestData(Prefs.accessToken)) as ResBase }
        Log.i(TAG, "startCheckToken: ${Prefs.accessToken}")


        val response = api.startCheckToken()
        response.call() {
            onSuccess = {
                Log.i(TAG, "startCheckToken: $it")
                if(it.isValid){
                    //main으로 넘김
                    _moveEvent.postValue(MoveEvent.Splash(SplashEvent.MAIN))
                }else{
                    //만료시 리프레시 토큰 진행
                    startNewToken()
                }
            }
        }
    }

    private fun startNewToken() = viewModelScope.launch(exceptionHandler) {
        Log.i(TAG, "startNewToken: ${Prefs.accessToken}")


        val response = api.startNewToken(RefreshTokenRequestData(Prefs.refreshToken))
        response.call() {
            onSuccess = {
                Log.i(TAG, "startNewToken: $it")
                Prefs.initUserInfo(it)
                _moveEvent.postValue(MoveEvent.Splash(SplashEvent.MAIN))
            }
            onFailure = {
                Log.i(TAG, "startNewToken onFailure: $it")
                Prefs.initUserInfo()
                _moveEvent.postValue(MoveEvent.Splash(SplashEvent.LOGIN))
            }
            onNetWorkError = {
                Log.i(TAG, "startNewToken onNetWorkError ")
                Prefs.initUserInfo()
                _moveEvent.postValue(MoveEvent.Splash(SplashEvent.LOGIN))
            }
        }
    }


//    private fun postUserGuardianLogin() = viewModelScope.launch(exceptionHandler) {
//        val result = callApi { api.postUserGuardianLogin(ReqUserLogin()) }
//
//        Prefs.appLocale = result.appLang
//        _moveEvent.postValue(MoveEvent.Splash(SplashEvent.MAIN_GUARDIAN))
//    }
//
//    private fun postUserOwnerLogin() = viewModelScope.launch(exceptionHandler) {
//        val result = callApi { api.postUserOwnerLogin(ReqUserLogin()) }
//
//        Prefs.appLocale = result.appLang
//        Prefs.setUserInfo(result)
//        when(app.getContext().getActivityTypeForAISuni()) {
//            ActivityType.PERMISSION -> {
//                _moveEvent.postValue(MoveEvent.Splash(SplashEvent.PERMISSION))
//            }
//            else ->{
//                _moveEvent.postValue(MoveEvent.Splash(SplashEvent.MAIN_WEARER))
//            }
//        }
//    }

    //설치 경로 체크
//    fun checkInstallReferrerData(context: Context) {
//
//        //설치 경로 체크 메서드
//        referrerClient = InstallReferrerClient.newBuilder(context).build()
//        referrerClient.startConnection(object : InstallReferrerStateListener {
//
//            override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                when (responseCode) {
//                    InstallReferrerClient.InstallReferrerResponse.OK -> {
//                        // Connection established.
//                        Log.d("referrerClient", "Connection established")
//
//                        val response: ReferrerDetails = referrerClient.installReferrer
//                        val referrerUrl: String = response.installReferrer
//                        val referrerClickTime: Long = response.referrerClickTimestampSeconds
//                        val appInstallTime: Long = response.installBeginTimestampSeconds
//                        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
//
//                        //값을 어떻게 가져오는지 테스트 할 것
////                        Log.d("referrerClient", response.toString())
//
//                        Log.d("referrerClient", referrerUrl) // utm_source=google-play&utm_medium=organic
//                        Log.d("referrerClient", referrerClickTime.toString())
//                        Log.d("referrerClient", appInstallTime.toString()) // 시간  1718093501
//                        Log.d("referrerClient", instantExperienceLaunched.toString())
//
//                        // 타임스탬프를 Instant 객체로 변환
//                        val formattedDateTime = transUnixTimestamp(appInstallTime)
//                        Log.d("referrerClient", formattedDateTime.toString()) // 시간  1718093501
//
//                        //파라미터 분할
//                        val params = referrerUrl.split("&".toRegex()).dropLastWhile { it.isEmpty() }
//                            .toTypedArray()
//
//                        for (param in params) {
//                            val keyValue = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }
//                                .toTypedArray()
//                            if (keyValue.size == 2) {
//                                val key = keyValue[0]
//                                val value = keyValue[1]
//                                when (key) {
//                                    "utm_source" -> utmSource = value
//                                    "utm_medium" -> utmMedium = value
//                                    "utm_campaign" -> utmCampaign = value
//                                    "utm_content" -> utmContent = value
//                                    "utm_term" -> utmTerm = value
//                                }
//                            }
//                        }
//
//                        Log.d("TAG", "utm_source: $utmSource")
//                        Log.d("TAG", "utm_medium: $utmMedium")
//                        Log.d("TAG", "utm_campaign: $utmCampaign")
//                        Log.d("TAG", "utm_content: $utmContent")
//                        Log.d("TAG", "utm_term: $utmTerm")
//
//                        //쉐어드에 저장
//                        Prefs.setInstallReferrerData(MSInstallReferrer(0, utmSource, utmMedium, utmCampaign, utmContent, utmTerm, formattedDateTime, "D"))
//
//                        postInstallReferrerDataBeforeLogin()
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//                        // API not available on the current Play Store app.
//                        Log.d("referrerClient", "API not available on the current Play Store app.")
//
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//                        // Connection couldn't be established.
//                        Log.d("referrerClient", "Connection couldn't be established.")
//
//                    }
//                }
//            }
//
//            override fun onInstallReferrerServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//                Log.d("referrerClient", "Google Play by calling the startConnection() method.")
//
//            }
//        })
//    }

    //레퍼러 파라미터 전송
//    fun postInstallReferrerDataBeforeLogin() {
//        val response = api.postInstallReferrerDataBeforeLogin(ReqInstallReferrer())
//
//        response.call() {
//            onSuccess = {
//                //referrer_idx -> 쉐어드에 저장
//                Prefs.setInstallReferrerData(MSInstallReferrer(it.referrerIdx, Prefs.getInstallReferrerData().utmSource, Prefs.getInstallReferrerData().utmMedium, Prefs.getInstallReferrerData().utmCampaign, Prefs.getInstallReferrerData().utmContent, Prefs.getInstallReferrerData().utmTerm, Prefs.getInstallReferrerData().referrerDateTime, Prefs.getInstallReferrerData().stateType))
//
//                Log.d("response", it.toString())
//            }
//        }
//    }
//
//    fun transUnixTimestamp(appInstallTime:Long): String {
//        // 타임스탬프를 Instant 객체로 변환
//        val instant = Instant.ofEpochSecond(appInstallTime)
//
//        // Instant 객체를 시스템 기본 타임존의 LocalDateTime 객체로 변환
//        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
//
//        // 날짜와 시간을 형식에 맞게 출력
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        val formattedDateTime = dateTime.format(formatter)
//
//        return formattedDateTime
//    }
}