package com.example.thoughts_cleaning.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class TrulyGenericViewModelFactory (
    private val mContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

//        if(modelClass.isAssignableFrom(RecordStageFragmentViewModel::class.java)){
//            return RecordStageFragmentViewModel(mContext = mContext) as T
//        }
//        throw IllegalArgumentException()

        // Java Class를 Kotlin KClass로 변환
        val kClass = modelClass.kotlin as KClass<T>

        // 주 생성자(Primary Constructor)를 찾습니다.
        val constructor = kClass.primaryConstructor
            ?: throw IllegalArgumentException("ViewModel $modelClass must have a primary constructor.")

        // 생성자 파라미터가 1개인지 확인 (Application 또는 Context)
        if (constructor.parameters.size != 1) {
            throw IllegalArgumentException(
                "ViewModel ${modelClass.simpleName} must have exactly one constructor parameter (Application/Context)."
            )
        }

        // 1개의 파라미터 (Application)를 전달합니다.
        val parameters = mapOf(
            constructor.parameters[0] to mContext
        )

        try {
            // 리플렉션을 통해 생성자를 호출하고 파라미터를 전달합니다.
            return constructor.callBy(parameters)
        } catch (e: Exception) {
            throw RuntimeException(
                "Failed to create ViewModel instance for ${modelClass.simpleName}. Check constructor arguments.", e
            )
        }





        // 1. Java Class를 Kotlin KClass로 변환
//        val kClass = modelClass.kotlin
//
//        // 2. 주 생성자(Primary Constructor)를 찾습니다.
//        val constructor = kClass.primaryConstructor
//            ?: throw IllegalArgumentException("ViewModel $modelClass must have a primary constructor.")
//
//        // 3. 생성자에 전달할 파라미터 맵을 준비합니다.
//        // 이 팩토리는 Application과 String 타입의 파라미터만 처리할 수 있습니다.
//        val parameters = mapOf(
//            constructor.parameters[0] to application, // 첫 번째 인자는 Application이라 가정
//            constructor.parameters[1] to param        // 두 번째 인자는 String이라 가정
//        )

//        try {
//            // 4. 리플렉션을 통해 생성자를 호출하고 파라미터를 전달합니다.
//            return constructor.callBy(parameters)
//        } catch (e: Exception) {
//            // 생성자 인자가 예상과 다르거나 다른 리플렉션 오류가 발생한 경우 처리
//            throw RuntimeException("Failed to create ViewModel instance for $modelClass. Check constructor arguments (Expected: Application, String).", e)
//        }
    }


}