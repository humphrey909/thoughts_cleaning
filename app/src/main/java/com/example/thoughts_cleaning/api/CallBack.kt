package com.example.thoughts_cleaning.api

import android.util.Log
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.util.base.BaseContract
import com.example.thoughts_cleaning.util.base.CallBackFailure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.java
import kotlin.jvm.javaClass
import kotlin.let

/**
 * Created by SeoKang on 2021-05-24.
 */
class CallBack<T>() : Callback<T> {
    var view: BaseContract.CallBackMethod? = null
    var failure: CallBackFailure? = null

    constructor(view: BaseContract.CallBackMethod) : this() {
        this.view = view
    }

    constructor(failure: CallBackFailure) : this() {
        this.failure = failure
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        view?.hideProgress()

        val body: T? = response.body()

        if (response.isSuccessful) {
            (body as? ResBase)?.let {
                when {
                    it.result == Constants.KEY_OK -> {
                        onSuccess?.invoke(body)
                    }
                    it.errorCode == Constants.ERROR_CODE_NO_VTS -> {
                        onFailureNoVts.invoke(it.errorMessage)
                    }
                    it.errorCode == Constants.ERROR_CODE_NEED_APP_UPDATE -> {
                        onFailureNeedAppUpdate.invoke()
                    }
                    it.errorCode == Constants.ERROR_CODE_SWER_005 -> { // 세션키 만료
//                        Prefs.initUserInfo()
                        onFailureInvalidSessionKey.invoke(it.errorMessage)
                    }
                    it.errorCode == Constants.ERROR_CODE_XCER_105 -> {
                        onFailureCustomTag.invoke(it.errorMessage)
                    }
                    it.errorCode == Constants.ERROR_CODE_XCER_109 -> {
                        onFailureResBase.invoke(it)
                    }
                    it.errorCode == Constants.ERROR_CODE_XC_EMERGENCY_01 -> {
                        onFailureResBase.invoke(it)
                    }
                    it.errorCode == Constants.ERROR_CODE_XC_EMERGENCY_02 -> {
                        onFailureResBase.invoke(it)
                    }
                    else -> {
                        onFailure.invoke(it.errorMessage)
                    }
                }
            }
            return
        }

        onNetWorkError.invoke()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        t.printStackTrace()
        view?.hideProgress()
        onNetWorkError.invoke()
    }

    var onSuccess: ((data: T) -> Unit)? = null

    var onFailure: ((errorMessage: String) -> Unit) =
        { errorMessage ->
            Log.d(javaClass.simpleName, "onFailure")
            view?.onFailure(errorMessage)
        }
    var onFailureResBase: ((error: ResBase) -> Unit) =
        { error ->
            Log.d(javaClass.simpleName, "onFailure")
            view?.onFailureResBase(error)
        }

    var onNetWorkError: (() -> Unit) = {
        Log.d(javaClass.simpleName, "onNetWorkError")
        view?.onNetworkError()

    }
    var onFailureNeedAppUpdate: (() -> Unit) = {
        Log.d(javaClass.simpleName, "onFailureNeedAppUpdate")
        view?.onFailureNeedAppUpdate()
        failure?.onFailureNeedAppUpdate()
    }

    var onFailureInvalidSessionKey: ((errorMessage: String) -> Unit) = {
        Log.d(javaClass.simpleName, "onFailureInvalidSessionKey")
//        view?.onFailureInvalidSessionKey(it, StartActivity::class.java)
//        failure?.onFailureInvalidSessionKey(it, StartActivity::class.java)
    }

    var onFailureNoVts: ((errorMessage: String) -> Unit) = {
        Log.d(javaClass.simpleName, "onFailureNoVts")
        view?.onFailure(it)
    }

    var onFailureCustomTag: ((errorMessage: String) -> Unit) = {
        Log.d(javaClass.simpleName, "onFailureNeedAppUpdate")
        view?.onFailure(it)
    }
}