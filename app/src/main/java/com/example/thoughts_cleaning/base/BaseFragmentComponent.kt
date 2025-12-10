package com.example.thoughts_cleaning.base

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.thoughts_cleaning.common.ErrorCodeApp
import com.example.thoughts_cleaning.common.vm.BaseViewModel

/**
 * Created by SeoKang on 2023-09-20.
 *
 * Composite Pattern
 */
interface BaseFragmentComponent<T: ViewDataBinding, S: BaseViewModel> {
    val layoutId: Int
    var _binding: T?
    val binding: T
    val viewModel: S
    fun onCreateViewImpl(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding?.root?.setOnClickListener { hideKeyboard() }
        return _binding?.root
    }

    fun onDestroyViewImpl() { _binding = null }

    fun showNetworkDialog(onConfirmListener:(() -> Unit)?)
    fun showMessageToast(message: String)
    fun showErrorToast(errorCode: ErrorCodeApp)
    fun goView(intent: Intent, isFinish: Boolean)
    fun showKeyboard(view: View)
    fun hideKeyboard(view: View? = null)
}
fun Activity.setStatusBarColor(isLightStatusBar: Boolean, statusBarColor: Int) {
    val window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = isLightStatusBar
    }
    window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
}