package com.example.thoughts_cleaning

import android.app.Application
import android.util.Log
import com.example.thoughts_cleaning.api.Prefs

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // 앱이 시작될 때 PreferenceManager를 초기화합니다.
        Prefs.init(applicationContext)
        Log.d("PreferenceManager", "초기화 완료")

        Prefs.clearAll()
    }

}