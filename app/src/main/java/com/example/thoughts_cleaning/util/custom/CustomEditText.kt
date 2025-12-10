package com.example.thoughts_cleaning.util.custom

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by SeoKang on 2021-05-26.
 */
class CustomEditText : AppCompatEditText {
    var onBackPress: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var textChangeListener: ((String) -> Unit)? = null


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP) {
            onBackPress?.invoke()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    fun setOnBackPressListener(onBackPress: () -> Unit) {
        this.onBackPress = onBackPress
    }


    private fun init() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No operation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No operation
            }

            override fun afterTextChanged(s: Editable?) {
                textChangeListener?.invoke(s.toString())
            }
        })
    }
    fun setTextChangeListener(listener: (String) -> Unit) {
        this.textChangeListener = listener
    }
}