package com.example.thoughts_cleaning.views.record_problem

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thoughts_cleaning.R

object RecordProblemBindingAdapters {

    @JvmStatic
    @BindingAdapter("check_bnt_state")
    fun checkBtnState(view: androidx.appcompat.widget.AppCompatButton, state : Boolean) {

        Log.i("checkBtnState", ": item clicked $state")


        if(!state){
            view.setBackgroundResource(R.drawable.btn_default_color_normal)
        }else{
            view.setBackgroundResource(R.drawable.btn_default_color_pressed)
        }
    }

}