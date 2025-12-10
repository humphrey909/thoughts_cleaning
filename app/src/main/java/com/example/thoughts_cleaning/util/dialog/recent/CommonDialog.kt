package com.example.thoughts_cleaning.util.dialog.recent

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.DialogCommonOneButtonBinding
import com.example.thoughts_cleaning.databinding.DialogCommonSubTitleBinding
import com.example.thoughts_cleaning.databinding.DialogCommonTwoButtonBinding
import com.example.thoughts_cleaning.databinding.DialogCommonTwoButtonImageBinding
import kr.dnx.ble.android.touchcare.library.views.custom.dialog.recent.CommonDialogType

/**
 * Created by SeoKang on 2023-02-20.
 * Created by Humphrey on 2024-07-09.
 */

class CommonDialog constructor(val builder: CommonDialogBuilder) : Dialog(builder.context) {
    val type = builder.type

    var onConfirmListener = builder.onConfirmListener
    var onCancelListener = builder.onCancelListener

    var imgUrl = builder.imgUrl

    var title: String = builder.title
    var titleSub = builder.titleSub
    var main = builder.main

    var confirmText = builder.confirmText
    var cancelText = builder.cancelText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val lpWindow: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window?.attributes = lpWindow

        when(type){
            CommonDialogType.ONE_BUTTON -> setOneButtonDialogView(DataBindingUtil.inflate(LayoutInflater.from(builder.context), R.layout.dialog_common_one_button, null, false))
            CommonDialogType.TWO_BUTTON -> setTwoButtonDialogView(DataBindingUtil.inflate(LayoutInflater.from(builder.context), R.layout.dialog_common_two_button, null, false))
            CommonDialogType.SUB_TITLE -> setSubTitleDialogView(DataBindingUtil.inflate(LayoutInflater.from(builder.context), R.layout.dialog_common_sub_title, null, false))
            CommonDialogType.TWO_BUTTON_IMAGE -> setTwoButtonImageDialogView(DataBindingUtil.inflate(LayoutInflater.from(builder.context), R.layout.dialog_common_two_button_image, null, false))
        }
        setCancelable(false)
    }

    private fun setOneButtonDialogView(binding : DialogCommonOneButtonBinding) {
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun setTwoButtonDialogView(binding: DialogCommonTwoButtonBinding) {
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun setSubTitleDialogView(binding: DialogCommonSubTitleBinding) {
        setContentView(binding.root)
        binding.dialog = this
    }

    private fun setTwoButtonImageDialogView(binding: DialogCommonTwoButtonImageBinding) {
        setContentView(binding.root)
        binding.dialog = this
    }

    fun onConfirmClick() {
        onConfirmListener?.invoke()
        dismiss()
    }

    fun onCancelClick() {
        onCancelListener?.invoke()
        dismiss()
    }
}