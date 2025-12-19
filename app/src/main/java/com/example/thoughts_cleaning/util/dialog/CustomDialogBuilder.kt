package com.example.thoughts_cleaning.util.dialog

import android.content.Context
import android.graphics.Color
import com.example.thoughts_cleaning.R

/**
 *
 */
class CustomDialogBuilder(val context: Context, var layoutId: Int = R.layout.dialog_custom_general_layout) {
    var onConfirmListener: (() -> Unit)? = null
    var onCancelListener: (() -> Unit)? = null
    var title: String = ""
    var titleColor: Int = Color.parseColor("#000000")
    var body: String = ""
    var buttonCount: Int = 1
    var color: DialogColorType = DialogColorType.DEFAULT
    var cancelText: String = context.getString(R.string.dialog_button_cancel)
    var confirmText: String = context.getString(R.string.dialog_button_confirm)

    fun build() = CustomDialog(this)

    fun onConfirmListener(onConfirmListener: (() -> Unit)?): CustomDialogBuilder {
        this.onConfirmListener = onConfirmListener
        return this
    }

    fun onCancelListener(onCancelListener: () -> Unit): CustomDialogBuilder {
        this.onCancelListener = onCancelListener
        return this
    }

    fun title(title: String): CustomDialogBuilder {
        this.title = title
        return this
    }

    fun titleColor(color: Int): CustomDialogBuilder {
        this.titleColor = color
        return this
    }

    fun body(body: String): CustomDialogBuilder {
        this.body = body
        return this
    }

    fun buttonCount(buttonCount: Int): CustomDialogBuilder {
        this.buttonCount = buttonCount
        return this
    }

    fun color(color: DialogColorType): CustomDialogBuilder {
        this.color = color
        return this
    }

    fun cancelText(cancelText: String): CustomDialogBuilder {
        this.cancelText = cancelText
        return this
    }

    fun confirmText(confirmText: String): CustomDialogBuilder {
        this.confirmText = confirmText
        return this
    }
}