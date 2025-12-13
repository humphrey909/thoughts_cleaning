package com.example.thoughts_cleaning.util

import android.content.Context
import com.example.thoughts_cleaning.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by SeoKang on 2021-05-21.
 */

private const val SEC: Int = 60
private const val MIN = 60
private const val HOUR = 24
private const val DAY = 30
private const val MONTH = 12

object TimeUtils {

    fun getCurrentNow(pattern: String = ""): String {
        return pattern.getCurrentDateForFormat()
    }

    fun getTimeZone(): Int {
        val tz = TimeZone.getDefault()
        var a = tz.rawOffset
        a = a / 1000 / 60 / 60
        return a
    }

    fun getCurrentDate(dfPattern: String): String {
        var pattern:String = dfPattern
        if (pattern.isEmpty()) {
            pattern = "yyyyMMddHHmmss"
        }
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun convertDateFormat(dateStr: String, originPattern: String, convertedPattern: String): String {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(originPattern))
            .format(DateTimeFormatter.ofPattern(convertedPattern))
    }

    fun convertDateTimeFormat(dateStr: String, originPattern: String, convertedPattern: String): String {
        return if(dateStr.isNotEmpty())
            LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(originPattern))
                .format(DateTimeFormatter.ofPattern(convertedPattern))
        else ""
    }
}

fun LocalDate.toInt(): Int = (this.year * 10_000) + (this.monthValue * 100) + this.dayOfMonth

fun String.getCurrentDateForFormat(): String{
    val pattern = if (this.isNotEmpty()) { this }
    else { "yyyyMMddHHmmss" }

    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))
}

inline fun<reified T> String.convertDateFormat(beforeFormat:String, afterFormat:String): String {
    if (beforeFormat.isEmpty() || afterFormat.isEmpty())
        return ""

    val beforeFormatter = DateTimeFormatter.ofPattern(beforeFormat)
    val afterFormatter = DateTimeFormatter.ofPattern(afterFormat)

    return when (T::class) {
        LocalDate::class ->LocalDate.parse(this, beforeFormatter).format(afterFormatter)
        LocalTime::class -> LocalTime.parse(this, beforeFormatter).format(afterFormatter)
        else -> LocalDateTime.parse(this, beforeFormatter).format(afterFormatter)
    }
}

fun Context.calculateTime(date: LocalDateTime, isCalculateTypeAfter: Boolean): String {
    val curTime = LocalDateTime.now()
    val duration = Duration.between(date, curTime)

    var diffTime = duration.seconds
    when {
        diffTime < SEC -> {
            // after|before sec or now
            return this.getString(R.string.const_now) //"지금";
        }
        SEC.let { diffTime /= it; diffTime } < MIN -> {
            // after|before min
            return if (isCalculateTypeAfter) this.getString(
                R.string.const_minutes_passed,
                diffTime
            ) else this.getString(R.string.const_minutes_before, diffTime)
        }
        MIN.let { diffTime /= it; diffTime } < HOUR -> {
            // after|before hour
            return if (isCalculateTypeAfter) this.getString(
                R.string.const_hours_passed,
                diffTime
            ) else this.getString(R.string.const_hours_before, diffTime)
        }
        HOUR.let { diffTime /= it; diffTime } < DAY -> {
            // after|before day
            return if (isCalculateTypeAfter) this.getString(
                R.string.const_days_passed,
                diffTime
            ) else this.getString(R.string.const_days_before, diffTime)
        }
        DAY.let { diffTime /= it; diffTime } < MONTH -> {
            // after|before moth
            return if (isCalculateTypeAfter) this.getString(
                R.string.const_months_passed,
                diffTime
            ) else this.getString(R.string.const_months_before, diffTime)
        }
        else -> {
            // after|before year
            return if (isCalculateTypeAfter) this.getString(
                R.string.const_years_passed,
                diffTime
            ) else this.getString(R.string.const_years_before, diffTime)
        }
    }
}

fun String.toDate(pattern: String = "yyyyMMdd"): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))

fun LocalDate.toString(pattern: String = "yyyyMMdd"): String = this.format(DateTimeFormatter.ofPattern(pattern))