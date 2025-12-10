package com.example.thoughts_cleaning.util.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.util.dialog.CustomDialog
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialog

/**
 * Created by SeoKang on 2021-05-20.
 */
interface BaseContract {
    interface CallBackMethod: CallBackFailure {
        fun showProgress()
        fun hideProgress()
        fun onNetworkError()
        fun onFailure(errorMessage: String)

//        fun onFailureResBase(error: ResBase)
        fun showErrorToast(errorCode: ErrorCodeApp)
    }

    interface FragmentView : CallBackMethod {
        fun getViewActivity(): Activity?
        fun getViewContext(): Context?
    }

    interface NavigationMethod: CallBackMethod {
        fun goView(intent: Intent, isFinish: Boolean = true)
        fun hideKeyboard()
        fun showDialog(dialog: CustomDialog)
        fun showDialog(dialog: CommonDialog)

//        fun goFragment(navFragmentId: Int)
//        fun restartActivity()
//        fun goFirstStack()
    }

    interface NavMethod {
        fun showNetworkDialog(onConfirmListener:(() -> Unit)?)
        fun showErrorToast(errorCode: ErrorCodeApp)
        fun showMessageToast(message: String)
        fun showProgress()
        fun hideProgress()
        fun showDialog(dialog: CustomDialog)
        fun showDialog(dialog: CommonDialog)
        fun hideDialog()
        fun goView(intent: Intent, isFinish: Boolean = true)
        fun showKeyboard(view: View)
        fun hideKeyboard()
    }
}