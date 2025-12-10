package com.example.thoughts_cleaning.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Geocoder
import android.os.BatteryManager
import android.os.BatteryManager.*
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import com.example.thoughts_cleaning.common.ErrorCodeApp
import java.io.IOException
import java.util.*

/**
 * Created by SeoKang on 2021-05-21.
 */


/**
 * 앱 버전 가져오기
 */
fun Context.isScreenOn(): Boolean {
    val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isInteractive
}

fun Context.isCharging(): Boolean {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { intentFilter ->
        this.registerReceiver(null, intentFilter)
    }
    val status: Int = batteryStatus?.getIntExtra(EXTRA_STATUS, -1) ?: -1

    return status == BATTERY_STATUS_CHARGING || status == BATTERY_STATUS_FULL
}

fun Context.getVersionName(): String? {

//    val packageManager: PackageManager = this.packageManager
//    packageManager.getPackageInfo(this.packageName, 0).versionName
//
//    return packageName
    val packageManager: PackageManager = this.packageManager

    return packageManager.getPackageInfo(this.packageName, 0).versionName

//    return try {
//        val packageManager: PackageManager = this.packageManager
//        packageManager.getPackageInfo(this.packageName, 0).versionName
//    } catch (e: PackageManager.NameNotFoundException) {
//        e.printStackTrace()
//        "..NameNotFound"
//    }
}

fun Context.getBatteryLevel(): Int {
    val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
    return bm.getIntProperty(BATTERY_PROPERTY_CAPACITY)
}

fun Context.getLanguage(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0].language
    } else {
        resources.configuration.locale.language
    }
}

/**
 * @return LanguageCode-CountryCode (ex. `kor-KOR`, `eng-USA`, `eng-GBR`)
 */
fun Context.getISOLanguage(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0].isO3Language + "-" +
                resources.configuration.locales[0].isO3Country
    } else {
        resources.configuration.locale.isO3Language + "-" +
                resources.configuration.locale.isO3Country
    }
}

fun Context.setLocale(language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)

    return createConfigurationContext(config)
}

fun Context.showToast(message: String) {
    if (message.isEmpty()) return

    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

//@SuppressLint("HardwareIds")
//fun Context.getDeviceId(): String {
//    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
//}

//버전 34 올리면서 같은 함수가 존재하여 못받아오는 경우가 있어서 생성
@SuppressLint("HardwareIds")
fun Context.getDeviceIdNew(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
}

fun Context.getAddress(longitude:Double, latitude: Double): String {
    try{
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(latitude, longitude, 1) ?: return ""

        if (list.isNotEmpty()) {
            return list[0].getAddressLine(0)
        }
    }catch (e: IOException){
        e.printStackTrace()
        return ""
    }

    return ""
}

fun Context.makeBitmapImage(resourceId: Int, text: String = ""): Bitmap{
    val scale = resources.displayMetrics.density
    var bitmap = BitmapFactory.decodeResource(resources, resourceId)

    var bitmapConfig = bitmap.config
    // set default bitmap config if none
    if (bitmapConfig == null) {
        bitmapConfig = Bitmap.Config.ARGB_8888
    }
    // resource bitmaps are imutable, so we need to convert it to mutable one
    bitmap = bitmap.copy(bitmapConfig, true)

    val canvas = Canvas(bitmap)
    // new antialised Paint
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = Color.WHITE

    paint.textSize = 12 * scale // text size in pixels
    paint.setShadowLayer(1f, 0f, 1f, Color.WHITE) // text shadow

    // draw text to the Canvas center
    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    val x = (bitmap.width - bounds.width()) / 2
    val y = (bitmap.height + bounds.height()) / 2

    canvas.drawText(text, x.toFloat(), y.toFloat(), paint)

    return bitmap
}


fun Context.showToast(code: ErrorCodeApp) {
    val message: String = this.getString(code.resourceId)
    showToast(message)
}