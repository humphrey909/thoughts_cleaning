package com.example.thoughts_cleaning.common.extension

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.example.thoughts_cleaning.api.Prefs

/**
 * Created by SeoKang on 2021-06-02.
 */

fun String.makePhoneNumberFormat(): String{
    val pattern = "([\\d]{3})([\\d]{3,4})([\\d]{4})"
    return this.replace(pattern.toRegex(), "$1-$2-$3")
}

fun String.checkHttpUrl(): String {
    if (this.isEmpty()) return ""

    if (this.startsWith("https://") || this.startsWith("http://")) return this

    val url = if (this[0] == '/') this.substring(1) else this
    return Prefs.baseUrl + url
}

fun String.getHtmlText(): Spanned {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, 0)
    } else {
        Html.fromHtml(this)
    }
}