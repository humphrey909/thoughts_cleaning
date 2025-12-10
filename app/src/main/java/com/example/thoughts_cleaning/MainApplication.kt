package com.example.thoughts_cleaning

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.thoughts_cleaning.api.Prefs
import com.kakao.sdk.common.KakaoSdk

class MainApplication: Application() {

    companion object {
        const val TAG = "MainApplication"
        lateinit var instance: MainApplication private set

        fun getContext() : Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        // 앱이 시작될 때 PreferenceManager를 초기화합니다.
        Prefs.init(applicationContext)
        Log.d("PreferenceManager", "초기화 완료")

        Prefs.clearAll()

        kakaoInit()
    }

    private fun kakaoInit(){
//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)

        val kakaoLoginNativeKey = getString(R.string.kakao_login_native_key)
        // Kakao SDK 초기화
        KakaoSdk.init(this, kakaoLoginNativeKey)
    }
}