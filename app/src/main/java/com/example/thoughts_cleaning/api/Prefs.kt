package com.example.thoughts_cleaning.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.google.gson.Gson

object Prefs {

    // 2. 사용할 SharedPreferences 파일명 정의
    private const val PREFS_NAME = "prefs"

    // 3. Context를 받아 초기화하는 지연 초기화 변수
    // 반드시 Application 클래스에서 최초 1회 초기화가 필요합니다.
    private lateinit var prefs: SharedPreferences

    // 4. 초기화 메서드
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    private class PreferenceDelegate<T>(private val key: String, private val defaultValue: T) : ReadWriteProperty<Any?, T> {
        private val gson = Gson()
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return with(prefs) {
                when (defaultValue) {
                    is String -> getString(key, defaultValue) as T
                    is Int -> getInt(key, defaultValue) as T
                    is Boolean -> getBoolean(key, defaultValue) as T
                    is Long -> getLong(key, defaultValue) as T
                    is Float -> getFloat(key, defaultValue) as T
                    is ArrayList<*> -> {
                        val json = getString(key, null)
                        if (json == null) {
                            defaultValue // 저장된 값이 없으면 기본값 반환
                        } else {
                            // JSON 문자열을 ArrayList<String>으로 역직렬화
                            val type = object : TypeToken<ArrayList<String>>() {}.type
                            gson.fromJson<T>(json, type) ?: defaultValue
                        }
                    }
                    else -> throw IllegalArgumentException("Unsupported type")
                }
            }
        }

        override fun setValue(
            thisRef: Any?,
            property: KProperty<*>,
            value: T
        ) {
            prefs.edit {
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is ArrayList<*> -> {
                        // ArrayList를 JSON 문자열로 직렬화하여 String으로 저장
                        val json = gson.toJson(value)
                        putString(key, json)
                    }
                    else -> throw IllegalArgumentException("Unsupported type")
                }
            }

        }


    }

    // 6. 실제 Preference 변수 정의 (사용할 때의 변수명)
    // key 이름: "USER_TOKEN", 기본값: null
    var userToken by PreferenceDelegate<String?>("USER_TOKEN", null)

    // key 이름: "IS_FIRST_RUN", 기본값: true
    var isFirstRun by PreferenceDelegate("IS_FIRST_RUN", true)

    // key 이름: "SAVED_SCORE", 기본값: 0
    var savedScore by PreferenceDelegate("SAVED_SCORE", 0)


    var anxietyWrite by PreferenceDelegate("ANXIETY_WRITE", "")

    var anxietyWriteList by PreferenceDelegate("ANXIETY_WRITE", arrayListOf<String>())

    // 7. 특정 키만 삭제하는 함수
    fun removeKey(key: String) {
        prefs.edit { remove(key) }
    }

    // 8. 모든 데이터를 삭제하는 함수
    fun clearAll() {
        prefs.edit { clear() }
    }
}