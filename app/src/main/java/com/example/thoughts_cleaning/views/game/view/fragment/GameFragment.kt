package com.example.thoughts_cleaning.views.game.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.databinding.FragmentMainBinding
import com.example.thoughts_cleaning.dialog.QuestionInputDialog
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.util.JoystickState
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.main.view.activity.container.MainActivity
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.three.joystick.JoystickView

class GameFragment : Fragment() {

    private val viewModel: GameFragmentViewModel by viewModels()

    private lateinit var joystickView: JoystickView
    private var isStop = false

    private var screenWidth = 0
    private var screenHeight = 0

    private var MOVE_FACTOR = 0.5f

    private var prevAngle = 0
    private lateinit var prevImageResource: LiveData<Int>

    private lateinit var gameView: GameView
    private val joystickSimulator = JoystickState()

    // 1. View Binding 객체 선언 (null 허용)
    private var _binding: FragmentGameBinding? = null

    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root
        //return inflater.inflate(R.layout.fragment_main, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Context 가져오기 (Fragment에서는 requireContext()를 사용)
        val context = requireContext()

        // 2. 동적으로 FrameLayout 생성
        val containerLayout = FrameLayout(context)

        // 3. GameView 인스턴스 생성
        // Fragment를 리스너로 사용하려면 'this'를 인수로 전달합니다.
        // joystickSimulator는 미리 초기화되어 있어야 합니다.
        gameView = GameView(
            context, /* GameViewListener: */
            requireActivity() as GameActivity, this, joystickSimulator
        )

        // 4. 레이아웃 파라미터 정의 (MATCH_PARENT)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        // 5. containerLayout(FrameLayout)에 gameView 추가
//        containerLayout.addView(gameView, params)

        (binding.root as? ViewGroup)?.addView(gameView, params)



        // 1. 조이스틱 인스턴스 생성 및 레이아웃 설정
        // Activity Context(this) 대신 Fragment Context(requireContext())를 사용합니다.
        val joystickView = JoystickView(context, null).apply {
            layoutParams = FrameLayout.LayoutParams(300, 300).apply {
                // dpToPx 함수는 아래에 정의되어 있습니다.
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                setMargins(50, 50, 50, 50)
            }
        }

        // 2. 컨테이너 레이아웃에 조이스틱 뷰 추가
        // Activity의 containerLayout 대신 binding.root (Fragment의 루트 뷰)를 사용합니다.
        // 주의: binding.root는 ViewGroup 타입이므로 addView()를 사용할 수 있습니다.
        (binding.root as? FrameLayout)?.addView(joystickView)
//        binding.root.addView(joystickView)

        // 3. 조이스틱 움직임 감지 리스너 설정
        joystickView.setOnMoveListener(
            object : JoystickView.OnMoveListener {
                override fun onMove(angle: Float, strength: Float) {
                    // (기존 로직 유지)
                    joystickSimulator.update(angle, strength)
                }
            },
            // JoystickView.DEFAULT_UPDATE_INTERVAL는 상수라고 가정
            JoystickView.Companion.DEFAULT_UPDATE_INTERVAL
        )

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지
        _binding = null
    }

    fun showCustomDialog() {
//        val dialog = dialogCustom()
//        // supportFragmentManager 또는 childFragmentManager를 사용합니다.
//        dialog.show(supportFragmentManager, "MyCustomDialogTag")

        // 1. 아직 묻지 않은 질문만 필터링
        val remainingQuestions = viewModel.allQuestions.filter { it !in viewModel.askedQuestions }

        if (remainingQuestions.isNotEmpty()) {
            // 2. 남은 질문 중 랜덤으로 하나 선택
            val nextQuestion = remainingQuestions.random()

            // 3. 질문 사용 처리
            viewModel.askedQuestions.add(nextQuestion)

            // 4. DialogFragment 생성 및 표시
            val dialog = QuestionInputDialog.Companion.newInstance(nextQuestion)
            dialog.show(requireActivity().supportFragmentManager, "QuestionDialog")

        } else {
            // 모든 질문을 다 소진했을 때의 처리
            Toast.makeText(requireContext(), "모든 질문을 완료했습니다!", Toast.LENGTH_LONG).show()
        }
    }
}