package com.example.thoughts_cleaning.util.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

/**
 * Created by humphrey on 2024/07/09.
 */
object BindingAdapters {

    //다이얼로그 메인 이미지 변경
    @JvmStatic
    @BindingAdapter("url_image")
    fun setUrlImage(view: ImageView, url: String) {
        if (url.isEmpty()) return
        val urlTrim = url.trim()

        Glide.with(view.rootView)
            .load(urlTrim)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(view)
    }
}