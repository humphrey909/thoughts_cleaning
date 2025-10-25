package com.example.thoughts_cleaning.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.thoughts_cleaning.R
import android.widget.Button
import android.widget.TextView

class QuestionInputDialog : DialogFragment() {

    // 다이얼로그에 커스텀 레이아웃을 연결합니다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 다이얼로그 창의 배경과 제목 표시줄을 제거하여 커스텀 레이아웃만 보이게 합니다.
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_custom, container, false)
    }

    companion object {
        private const val ARG_QUESTION = "question_text"

        fun newInstance(question: String): QuestionInputDialog {
            val fragment = QuestionInputDialog()
            val args = Bundle().apply {
                putString(ARG_QUESTION, question)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 전달받은 질문 텍스트 가져오기
        val question = arguments?.getString(ARG_QUESTION) ?: "질문을 불러오지 못했습니다."

        val questionTextView = view.findViewById<TextView>(R.id.dialog_question_text)
        questionTextView.text = question

        view.findViewById<Button>(R.id.dialog_cancel_button)?.setOnClickListener {
            // 이 코드는 버튼이 속한 Fragment(DialogFragment)에서 실행됩니다.
            dismiss() // 다이얼로그를 안전하게 닫습니다.
        }
        view.findViewById<Button>(R.id.dialog_confirm_button)?.setOnClickListener {
            // 이 코드는 버튼이 속한 Fragment(DialogFragment)에서 실행됩니다.
            dismiss() // 다이얼로그를 안전하게 닫습니다.
        }
    }

//    fun onCloseDialogClicked(view: View) {
//        // 🌟 이 부분이 버튼 클릭 시 실행됩니다.
//        dismiss()
//
//        // 예시: 버튼이 클릭되면 다이얼로그를 닫는 로직
//        // (Fragment에서 사용된다고 가정 시: parentFragmentManager.beginTransaction().remove(this).commit())
//        // Activity에서 다이얼로그를 관리하는 경우: dialogReference.dismiss()
//
//        // 만약 이 버튼이 DialogFragment 내부에 있다면:
//        // (this as? MyCustomDialog)?.dismiss()
//    }
}