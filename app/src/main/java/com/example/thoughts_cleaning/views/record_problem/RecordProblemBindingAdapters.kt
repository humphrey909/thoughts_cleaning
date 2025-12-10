package com.example.thoughts_cleaning.views.record_problem

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.RecordStageFragmentViewModel

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



    @JvmStatic
    @BindingAdapter("change_background")
    fun changeBackground(view: ConstraintLayout, currentMainFlow : RecordStageFragmentViewModel.RecordStageFlow) {

//        if(currentMainFlow == RecordStageFragmentViewModel.RecordStageFlow.STAGE_3){
////            view.setBackgroundResource(R.drawable.smudge_texture)
////            view.setBackgroundColor(Color.parseColor("#00000000"))
//            view.setBackgroundResource(R.drawable.basic_background)
////            view.setBackgroundColor(color)
//        }else{
//            //basic_background
////            view.setBackgroundColor(Color.parseColor("#E7E4B4"))
//            view.setBackgroundResource(R.drawable.basic_background)
//        }
    }

}