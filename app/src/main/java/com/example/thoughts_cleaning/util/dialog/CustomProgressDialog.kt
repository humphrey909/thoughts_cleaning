package com.example.thoughts_cleaning.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.DialogCustomProgressLayoutBinding

class CustomProgressDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogCustomProgressLayoutBinding
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.3f)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_custom_progress_layout, null, false)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(context, R.anim.progress)
        binding.progressImage.animation = anim
    }
}