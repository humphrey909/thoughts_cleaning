package com.example.thoughts_cleaning.common.extension

import com.example.thoughts_cleaning.api.CallBack
import com.example.thoughts_cleaning.util.base.BaseContract
import com.example.thoughts_cleaning.util.base.CallBackFailure
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call

/**
 * Created by SeoKang on 2021-05-24.
 */
fun<T> Call<T>.call(view: BaseContract.CallBackMethod, callback: CallBack<T>.() -> Unit){
    view.showProgress()
    val customCallBack: CallBack<T> = CallBack(view)
    callback.invoke(customCallBack)
    this.enqueue(customCallBack)
}

fun<T> Call<T>.call(failure: CallBackFailure, callback: CallBack<T>.() -> Unit){
    val customCallBack: CallBack<T> = CallBack(failure)
    callback.invoke(customCallBack)
    this.enqueue(customCallBack)
}

fun<T> Call<T>.call(failure: CallBackFailure){
    val customCallBack: CallBack<T> = CallBack(failure)
    this.enqueue(customCallBack)
}

fun<T> Call<T>.call(){
    val customCallBack: CallBack<T> = CallBack()
    this.enqueue(customCallBack)
}

fun<T> Call<T>.call(callback: CallBack<T>.() -> Unit){
    val customCallBack: CallBack<T> = CallBack()
    callback.invoke(customCallBack)
    this.enqueue(customCallBack)
}




