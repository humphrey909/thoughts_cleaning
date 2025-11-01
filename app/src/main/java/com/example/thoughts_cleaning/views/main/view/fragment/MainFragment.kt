package com.example.thoughts_cleaning.views.main.view.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thoughts_cleaning.views.main.vm.fragment.MainFragmentViewModel
import com.example.thoughts_cleaning.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()

//    private lateinit var joystickView: JoystickView
//    private var isStop = false
//
//    private var screenWidth = 0
//    private var screenHeight = 0
//
//    private var MOVE_FACTOR = 0.5f
//
//    private var prevAngle = 0
//    private lateinit var prevImageResource: LiveData<Int>
//
//    private lateinit var gameView: GameView
//    private val joystickSimulator = JoystickState()

    // 1. View Binding 객체 선언 (null 허용)
    private var _binding: FragmentMainBinding? = null

    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
        //return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initNavigation()

//        val navHostFragment = childFragmentManager
//            .findFragmentById(R.id.)
//        val navHostFragment = binding.mainContentNavHost
//
//
//        if (navHostFragment is NavHostFragment) {
//            // 3. 찾은 객체가 NavHostFragment 타입인 것이 확인되면 처리
//            val navController = navHostFragment.navController
//
//            // Binding 객체를 사용하여 BottomNavigationView에 접근
//            binding.bottomNavInFragment.setupWithNavController(navController)
//
//        } else {
//            // 디버깅을 위한 로그 출력 (객체를 찾지 못했거나 타입이 틀린 경우)
////            Log.e("MainFragment", "NavHostFragment를 찾지 못했거나 타입이 올바르지 않습니다. ID 확인 필수!")
//        }




//        val navView: BottomNavigationView = binding.bottomNavInFragment
////        val mainFragmentContentNav: FragmentContainerView = binding.mainContentNavHost
//
////        val navView: BottomNavigationView = view.findViewById(R.id.bottom_nav_in_fragment)
//
//        // **자식 Fragment Manager 사용**
//        val navHostFragment = childFragmentManager
//            .findFragmentById(R.id.main_content_nav_host) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        // BottomNavigationView와 NavController를 연결
////        navView.setupWithNavController(navController)
//
//        binding.bottomNavInFragment.setupWithNavController(navController)

//        // 1. Context 가져오기 (Fragment에서는 requireContext()를 사용)
//        val context = requireContext()
//
//        // 2. 동적으로 FrameLayout 생성
//        val containerLayout = FrameLayout(context)
//
//        // 3. GameView 인스턴스 생성
//        // Fragment를 리스너로 사용하려면 'this'를 인수로 전달합니다.
//        // joystickSimulator는 미리 초기화되어 있어야 합니다.
//        gameView = GameView(
//            context, /* GameViewListener: */
//            requireActivity() as MainActivity, this, joystickSimulator
//        )
//
//        // 4. 레이아웃 파라미터 정의 (MATCH_PARENT)
//        val params = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.MATCH_PARENT,
//            FrameLayout.LayoutParams.MATCH_PARENT
//        )
//
//        // 5. containerLayout(FrameLayout)에 gameView 추가
////        containerLayout.addView(gameView, params)
//
//        (binding.root as? ViewGroup)?.addView(gameView, params)
//
//
//
//        // 1. 조이스틱 인스턴스 생성 및 레이아웃 설정
//        // Activity Context(this) 대신 Fragment Context(requireContext())를 사용합니다.
//        val joystickView = JoystickView(context, null).apply {
//            layoutParams = FrameLayout.LayoutParams(300, 300).apply {
//                // dpToPx 함수는 아래에 정의되어 있습니다.
//                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//                setMargins(50, 50, 50, 50)
//            }
//        }
//
//        // 2. 컨테이너 레이아웃에 조이스틱 뷰 추가
//        // Activity의 containerLayout 대신 binding.root (Fragment의 루트 뷰)를 사용합니다.
//        // 주의: binding.root는 ViewGroup 타입이므로 addView()를 사용할 수 있습니다.
//        (binding.root as? FrameLayout)?.addView(joystickView)
////        binding.root.addView(joystickView)
//
//        // 3. 조이스틱 움직임 감지 리스너 설정
//        joystickView.setOnMoveListener(
//            object : JoystickView.OnMoveListener {
//                override fun onMove(angle: Float, strength: Float) {
//                    // (기존 로직 유지)
//                    joystickSimulator.update(angle, strength)
//                }
//            },
//            // JoystickView.DEFAULT_UPDATE_INTERVAL는 상수라고 가정
//            JoystickView.Companion.DEFAULT_UPDATE_INTERVAL
//        )

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지
        _binding = null
    }

//    fun showCustomDialog() {
////        val dialog = dialogCustom()
////        // supportFragmentManager 또는 childFragmentManager를 사용합니다.
////        dialog.show(supportFragmentManager, "MyCustomDialogTag")
//
//        // 1. 아직 묻지 않은 질문만 필터링
//        val remainingQuestions = viewModel.allQuestions.filter { it !in viewModel.askedQuestions }
//
//        if (remainingQuestions.isNotEmpty()) {
//            // 2. 남은 질문 중 랜덤으로 하나 선택
//            val nextQuestion = remainingQuestions.random()
//
//            // 3. 질문 사용 처리
//            viewModel.askedQuestions.add(nextQuestion)
//
//            // 4. DialogFragment 생성 및 표시
//            val dialog = QuestionInputDialog.Companion.newInstance(nextQuestion)
//            dialog.show(requireActivity().supportFragmentManager, "QuestionDialog")
//
//        } else {
//            // 모든 질문을 다 소진했을 때의 처리
//            Toast.makeText(requireContext(), "모든 질문을 완료했습니다!", Toast.LENGTH_LONG).show()
//        }
//    }

//
//    private fun initNavigation() {
//
//        binding.bottomNavInFragment.itemIconTintList = null
//        binding.bottomNavInFragment.run {
//            setOnItemSelectedListener { item ->
////                if(tabMenuSelectComplete == 0){
//                    when (item.itemId) {
////                        R.id.home_button -> {
////                            //결제 전, 후 페이지
//////                            viewModel._bottomSheetMenuValue.postValue(BottomSheetState.HOME)
////                        }
////                        R.id.set_button -> {
////                        }
//                }
//                true
//            }
//        }
//
//    }
}