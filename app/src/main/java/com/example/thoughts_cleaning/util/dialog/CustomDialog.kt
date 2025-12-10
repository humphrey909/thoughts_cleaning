package com.example.thoughts_cleaning.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.thoughts_cleaning.R

class CustomDialog constructor(builder: CustomDialogBuilder) : Dialog(builder.context) {
    val mContext: Context = builder.context
    var onConfirmListener: (() -> Unit)? = builder.onConfirmListener
    var onCancelListener: (() -> Unit)? = builder.onCancelListener

    var layoutId: Int = builder.layoutId
    var title: String = builder.title
    var titleColor: Int = builder.titleColor
    var body: String = builder.body
    var buttonCount: Int = builder.buttonCount
    var color: DialogColorType = builder.color
    var cancelText: String = builder.cancelText
    var confirmText: String = builder.confirmText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val lpWindow: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window?.attributes = lpWindow

        setContentView(layoutId)
        setCancelable(false)
        setBuilderData()
    }

    private fun setBuilderData() {
        setDialogText()
        setConfirmButton()
        setCancelButton()
    }

    private fun setDialogText() {
        val titleText = findViewById<TextView>(R.id.txt_title)
        val contentText = findViewById<TextView>(R.id.txt_content)
        titleText.text = title
        titleText.setTextColor(titleColor)
        contentText.text = body

        if (color == DialogColorType.RED) {
            contentText.setTextColor(Color.parseColor("#E96363"))
        }
    }

    private fun setConfirmButton() {
        val confirmButton = findViewById<Button>(R.id.btn_confirm)

        confirmButton.text = confirmText
        confirmButton.setOnClickListener {
            onConfirmListener?.invoke()
            dismiss()
        }

        if (color == DialogColorType.RED) {
            confirmButton.background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.button_red, null)
        }
    }

    private fun setCancelButton() {
        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        if (buttonCount == 1) {
            cancelButton.visibility = View.GONE
            return
        }

        cancelButton.visibility = View.VISIBLE
        cancelButton.text = cancelText
        cancelButton.setOnClickListener {
            onCancelListener?.invoke()
            dismiss()
        }
    }
}