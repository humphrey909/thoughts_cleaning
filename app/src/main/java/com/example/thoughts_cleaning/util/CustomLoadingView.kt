package com.example.thoughts_cleaning.util

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.example.thoughts_cleaning.R

class CustomLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        // 1. 배경색 설정 (SurfaceView의 검은 화면을 가리기 위해 불투명해야 함)
        // 원하는 색상으로 변경 가능 (예: Color.BLACK, Color.parseColor("#FF0000"))
//        setBackgroundColor(Color.WHITE)
        setBackgroundResource(R.drawable.room_background3)

        // 2. 프로그레스바 생성
        val progressBar = ProgressBar(context).apply {
            isIndeterminate = true // 뱅글뱅글 도는 애니메이션
        }

        // 3. 프로그레스바 위치 설정 (화면 정중앙)
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER // 부모(FrameLayout)의 중앙에 위치
        }

        // 4. 뷰에 추가
        addView(progressBar, params)

        // 초기에는 보이게 설정
        visibility = View.VISIBLE
    }

    // 편의 기능을 위한 함수 (배경색 변경이 필요할 때 호출)
    fun setBackgroundColorHex(hexColor: String) {
        try {
            setBackgroundColor(Color.parseColor(hexColor))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}