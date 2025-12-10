package com.example.thoughts_cleaning.base

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.util.setLocale
import com.example.thoughts_cleaning.common.util.showToast
import com.example.thoughts_cleaning.util.dialog.CustomDialog
import com.example.thoughts_cleaning.util.dialog.CustomDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialog
import com.example.thoughts_cleaning.util.dialog.CustomProgressDialog
import com.example.thoughts_cleaning.util.dialog.DialogColorType
import com.example.thoughts_cleaning.util.dialog.onDismiss
import com.example.thoughts_cleaning.util.dialog.onShow

/**
 * Created by SeoKang on 2021-05-20.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected abstract val appNameId: Int
    protected abstract val appLang: String

    private val progressDialog: CustomProgressDialog by lazy { CustomProgressDialog(this) }
    private var generalDialog: CustomDialog? = null
    private var commonDialog: CommonDialog? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.setLocale(appLang))
    }

    override fun onResume() {
        super.onResume()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    protected fun Activity.setStatusBarColor(isLightStatusBar: Boolean, statusBarColor: Int) {
        val window = this.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val wic = WindowInsetsControllerCompat(window, window.decorView)
            wic.isAppearanceLightStatusBars = isLightStatusBar
        }
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
    }

    override fun onStop() {
        super.onStop()
        generalDialog?.onDismiss(this)
        commonDialog?.onDismiss(this)
        progressDialog.onDismiss(this)
    }

    fun showProgress() {
        this.runOnUiThread { progressDialog.onShow(this) }
    }

    fun hideProgress() {
        this.runOnUiThread { progressDialog.onDismiss(this) }
    }

    fun showErrorToast(errorCode: ErrorCodeApp) {
        this.showToast(errorCode)
    }

    fun showNetworkDialog(onConfirmListener:(() -> Unit)?) {
        val dialog = CustomDialogBuilder(this)
            .title(getString(appNameId))
            .body(getString(R.string.dialog_network_error_message))
            .color(DialogColorType.RED)
            .onConfirmListener(onConfirmListener)
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

    fun showDialog(dialog: CommonDialog) {
        if (canMakeDialog(false)) {
            this.runOnUiThread {
                commonDialog = dialog
                commonDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
                commonDialog?.onShow(this)
            }
        }
    }

    fun hideDialog() {
        generalDialog?.onDismiss(this)
        commonDialog?.onDismiss(this)
    }

    fun goView(intent: Intent, isFinish: Boolean) {
        if (isFinish) finishAffinity()

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

    }

    fun showMessageToast(message: String) = showToast(message)
}