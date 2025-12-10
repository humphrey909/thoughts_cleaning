package com.example.thoughts_cleaning.util.dialog

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.thoughts_cleaning.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by SeoKang on 2021-05-26.
 */
fun Dialog.onDismiss(activity: Activity){
    if(!activity.isFinishing && this.isShowing){
        this.dismiss()
    }
}

fun Dialog.onShow(activity: Activity){
    if(!activity.isFinishing && !isShowing) {
        show()
    }
}

fun Dialog.onDismiss(fragment : Fragment) {
    if (fragment.isAdded) {
        fragment.activity?.let { it.runOnUiThread { onDismiss(it) } }
    }
}

fun Dialog.onShow(fragment : Fragment) {
    if (fragment.isAdded) {
        fragment.activity?.let { it.runOnUiThread { onShow(it)  } }
    }
}

//fun Context.getCountryPicker(selectValue: (String) -> Unit): AlertDialog? {
//    this.resources?.let {
//        val countryCodes: Array<String> = it.getStringArray(R.array.const_country_code)
//        val pickerView: View = View.inflate(this, R.layout.dialog_number_picker, null)
//        val picker = pickerView.findViewById<NumberPicker>(R.id.numPicker)
//        picker.minValue = 0
//        picker.maxValue = countryCodes.size - 1
//        picker.value = 0
//        picker.displayedValues = countryCodes
//
//        return AlertDialog.Builder(this)
//            .setView(pickerView)
//            .setPositiveButton(R.string.ok) { _, _ ->
//                var codes = countryCodes[picker.value]
//                codes = codes.substring(codes.indexOf("+"), codes.length - 1)
//
//                selectValue.invoke(codes)
//            }
//            .setNegativeButton(R.string.cancel, null)
//            .create()
//    }
//
//    return null
//}

fun Activity.getDatePicker(
    traceDate: String,
    format: String = "yyyy.MM.dd",
    selectValue: (LocalDate) -> Unit
): DatePickerDialog {
    val date: LocalDate = LocalDate.parse(traceDate, DateTimeFormatter.ofPattern(format))
    val dialog = DatePickerDialog(this, { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        selectValue.invoke(selectedDate)
    }, date.year, date.monthValue - 1, date.dayOfMonth)

    dialog.datePicker.maxDate = System.currentTimeMillis()

    return dialog
}


//fun Activity.getNumberPicker(
//    minValue: Int,
//    maxValue: Int,
//    selectedValue: Int,
//    selectValue: (Int) -> Unit
//): AlertDialog {
//    val pickerView: View = View.inflate(this, R.layout.dialog_number_picker, null)
//    val picker = pickerView.findViewById<NumberPicker>(R.id.numPicker)
//    picker.minValue = minValue
//    picker.maxValue = maxValue
//    picker.value = selectedValue
//
//    return AlertDialog.Builder(this)
//        .setView(pickerView)
//        .setPositiveButton(R.string.dialog_button_confirm) { _, _ -> selectValue.invoke(picker.value) }
//        .setNegativeButton(R.string.cancel, null)
//        .create()
//}

fun Activity.getTagAlarmTypePicker(tagAlarmTypes: Array<String>, selectValue: (Int) -> Unit): AlertDialog? {
    this.resources?.let {
        val pickerView: View = View.inflate(this, R.layout.dialog_number_picker, null)
        val picker = pickerView.findViewById<NumberPicker>(R.id.numPicker)
        picker.minValue = 0
        picker.maxValue = 2
        picker.displayedValues = tagAlarmTypes

        return AlertDialog.Builder(this)
            .setView(pickerView)
            .setPositiveButton(R.string.dialog_button_confirm) { _, _ ->
                selectValue.invoke(picker.value)
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .create()
    }

    return null
}

fun Activity.getTagGroupPicker(selectValue: (String) -> Unit): AlertDialog? {
    this.resources?.let {
        val tagGroups: Array<String> = it.getStringArray(R.array.const_country_code)
        val pickerView: View = View.inflate(this, R.layout.dialog_number_picker, null)
        val picker = pickerView.findViewById<NumberPicker>(R.id.numPicker)
        picker.minValue = 0
        picker.maxValue = 5
        picker.displayedValues = tagGroups

        return AlertDialog.Builder(this)
            .setView(pickerView)
            .setPositiveButton(R.string.dialog_button_confirm) { _, _ ->
                selectValue.invoke(tagGroups[picker.value])
            }
            .setNegativeButton(R.string.dialog_button_cancel, null)
            .create()
    }

    return null
}

