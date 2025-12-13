package com.example.thoughts_cleaning.base

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.setLocale
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.util.base.BaseContract
import com.example.thoughts_cleaning.util.dialog.CustomDialog
import com.example.thoughts_cleaning.util.dialog.CustomProgressDialog
import com.example.thoughts_cleaning.util.dialog.onDismiss
import com.example.thoughts_cleaning.util.dialog.onShow
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialog
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import kr.dnx.ble.android.touchcare.library.views.custom.dialog.recent.CommonDialogType


/**
 * Created by SeoKang on 2021-05-20.
 */
@Deprecated("Change MVVM")
abstract class BaseOldActivity : AppCompatActivity(), BaseContract.CallBackMethod{
    protected open val statusBarColorId: Int = R.color.white
    protected abstract val appNameId: Int
    protected abstract val appLang: String

    private lateinit var progressDialog: CustomProgressDialog
    private var generalDialog: CustomDialog? = null
    private var commonDialog: CommonDialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.setLocale(appLang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = true
        }
        this.window?.statusBarColor = ContextCompat.getColor(this, statusBarColorId)
        progressDialog = CustomProgressDialog(this)
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onStop() {
        super.onStop()
        generalDialog?.onDismiss(this)
        commonDialog?.onDismiss(this)
        progressDialog.onDismiss(this)
    }

    override fun showProgress() {
        this.runOnUiThread {
            progressDialog.onShow(this)
        }
    }

    override fun hideProgress() {
        this.runOnUiThread {
            progressDialog.onDismiss(this)
        }
    }

    override fun showErrorToast(errorCode: ErrorCodeApp) {
        this.showToast(errorCode)
    }

    override fun onFailureNeedAppUpdate() {
        Log.e("BaseActivity", "onFailureNeedAppUpdate")
    }

    override fun onFailureInvalidSessionKey(errorMessage: String, target: Class<*>) {
        onFailure(errorMessage)
        val intent = Intent(this, target)
        goView(intent)
    }

    override fun onFailure(errorMessage: String) {
        this.showToast(errorMessage)
    }

    override fun onFailureResBase(error: ResBase) {
        this.showToast(error.errorCode)
    }


    override fun onNetworkError() {
        val dialog = CommonDialogBuilder(this, CommonDialogType.ONE_BUTTON)
            .title(getString(appNameId))
            .main(getString(R.string.dialog_network_error_message))
            .build()

        showDialog(dialog)
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun canMakeDialog(isCustomDialog: Boolean): Boolean {
        return if (isCustomDialog) generalDialog?.isShowing != true && !this.isFinishing else commonDialog?.isShowing != true && !this.isFinishing
    }

    fun showDialog(dialog: CustomDialog){
        if (canMakeDialog(true)) {
            this.runOnUiThread {
                generalDialog = dialog
                generalDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
                generalDialog?.onShow(this)
            }
        }
    }

    fun showDialog(dialog: CommonDialog){
        if (canMakeDialog(false)) {
            this.runOnUiThread {
                commonDialog = dialog
                commonDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
                commonDialog?.onShow(this)
            }
        }
    }

    fun hideDialog() {
        generalDialog?.dismiss()
    }

    fun goView(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun goView(intent: Intent, isFinish: Boolean) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        if (isFinish) finish()
    }

    fun showMessageToast(message: String) = showToast(message)
}