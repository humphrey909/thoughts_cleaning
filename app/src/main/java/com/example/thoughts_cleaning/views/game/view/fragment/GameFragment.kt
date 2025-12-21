package com.example.thoughts_cleaning.views.game.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.util.dialog.QuestionInputDialog
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.util.ItemType
import com.example.thoughts_cleaning.util.JoystickState
import com.example.thoughts_cleaning.views.game.GameEvent
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.three.joystick.JoystickView

class GameFragment : MasilFragment<FragmentGameBinding, GameFragmentViewModel>(R.layout.fragment_game), GameView.GameActionListener {

    override val viewModel by viewModelFactory { GameFragmentViewModel() }

//    private val viewModel: GameFragmentViewModel by viewModels()

    private var joystickView: JoystickView? = null // 조이스틱 뷰 인스턴스
//    private var isStop = false
//
//    private var screenWidth = 0
//    private var screenHeight = 0
//
//    private var MOVE_FACTOR = 0.5f
//
//    private var JOYSTICK_SIZE_PX = 300
//
//    private var prevAngle = 0
//    private lateinit var prevImageResource: LiveData<Int>

    private lateinit var gameView: GameView
    private val joystickSimulator = JoystickState()

//    // 1. View Binding 객체 선언 (null 허용)
//    private var _binding: FragmentGameBinding? = null
//
//    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
//    private val binding get() = _binding!!


    // 터치 다운 시 조이스틱의 중심이 될 초기 위치
    private var initialTouchX = 0f
    private var initialTouchY = 0f

//    private lateinit var mContext: Context

//    private var wasteCount = 0

    private var uiLayer: ConstraintLayout? = null
    private var buttonGroup: LinearLayout? = null
    private val uiGroupMap = mutableMapOf<String, LinearLayout>()

    companion object {
        // 이 부분이 있어야 외부에서 BFragment.newInstance(...) 로 호출 가능합니다.
        private const val ARG_DATA_KEY = "waste_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Bundle 전체를 가져옵니다.
//        val bundle = arguments

        // 2. Bundle이 null이 아닌지 확인하고, 보낸 키(Key)를 사용하여 데이터를 추출합니다.
//        if (bundle != null) {
//            // 예시: "id_key"라는 키로 Int를 보냈을 경우
//            wasteCount = bundle.getInt("waste_count", 0) // Int는 기본값(0)을 지정하는 것이 안전합니다.
//            Log.d("waste_count", "waste_count: ${wasteCount}")
//        }

        // 1. Context 가져오기 (Fragment에서는 requireContext()를 사용)
//        mContext = requireContext()

        // 2. 동적으로 FrameLayout 생성
//        val containerLayout = FrameLayout(mContext)

        // 3. GameView 인스턴스 생성
        // Fragment를 리스너로 사용하려면 'this'를 인수로 전달합니다.
        // joystickSimulator는 미리 초기화되어 있어야 합니다.

        gameView = GameView(
            mContext, /* GameViewListener: */
            requireActivity() as GameActivity, this, joystickSimulator,
            viewModel.wasteCount
        )

        // 4. 레이아웃 파라미터 정의 (MATCH_PARENT)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        (binding.root as? ViewGroup)?.addView(gameView, params)

        binding.root.setOnTouchListener { v, event ->
            handleJoystickTouch(v, event)
            true // 이벤트 소비
        }

        gameView.setGameActionListener(this)

        handleNavigationEvent()
    }

    private fun handleNavigationEvent() {

        viewModel.moveEvent.observe(this.viewLifecycleOwner) {
            if (it is MoveEvent.Game) {
                when(it.moveType) {
                    GameEvent.COMMON -> {

                    }
                    GameEvent.CLEAN_BED -> {
//                        createCleanBtn(GameEvent.CLEAN_BED)
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_WINDOW -> {
//                        createCleanBtn(GameEvent.CLEAN_WINDOW)
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_DESK -> {
//                        createCleanBtn(GameEvent.CLEAN_DESK)
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_WARDROBE -> {
//                        createCleanBtn(GameEvent.CLEAN_WARDROBE)
                        visibleCleanBtnNew(it.moveType)
                    }

                    GameEvent.DISABLE_CLEAN_BTN -> {
                        disVisibleCleanBtnNew()
                    }
                }
            }
        }

        makeCleanBtnNew()
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

    /**
     * 화면 터치 이벤트에 따라 조이스틱의 상태를 관리하고 업데이트합니다.
     */
    private fun handleJoystickTouch(v: View, event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 1. 초기 터치 위치 저장 (조이스틱의 중심이 될 위치)
                initialTouchX = touchX
                initialTouchY = touchY

                // 2. 조이스틱 생성 및 레이아웃에 추가
                createAndAddJoystick(initialTouchX, initialTouchY)

                // 3. 조이스틱 스레드 시작
                joystickView?.startThread()
            }

            MotionEvent.ACTION_MOVE -> {
//                Log.d("JoystickFragment 222", "Angle: $touchX, Strength: $touchY")

                // 4. 조이스틱 위치 업데이트
//                joystickView?.updatePositionAndCenter(touchX, touchY, initialTouchX, initialTouchY)

                val touchX = event.x // 부모 뷰 기준 절대 X
                val touchY = event.y // 부모 뷰 기준 절대 Y

                // 뷰의 좌상단 좌표
                val viewLeft = joystickView!!.x
                val viewTop = joystickView!!.y

                // Fragment의 절대 좌표를 조이스틱 뷰 내부의 로컬 좌표로 변환
                val localX = touchX - viewLeft
                val localY = touchY - viewTop

                // 조이스틱의 중심점도 로컬 좌표로 변환 (터치 다운 시점의 절대 좌표 - 뷰의 좌상단)
                val localCenterX = initialTouchX - viewLeft
                val localCenterY = initialTouchY - viewTop

                // JoystickView 내부로 로컬 좌표 전달
                joystickView?.updatePositionAndCenter(localX, localY, localCenterX, localCenterY)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 5. 스레드 중단 및 조이스틱 중앙 복귀 (스프링 효과)
                joystickView?.stopThread()
                joystickView?.returnToCenter()

                // 6. 딜레이 후 조이스틱 제거
                (binding.root as? FrameLayout)?.removeView(joystickView)
                joystickView = null
            }
        }
        return true
    }

    /**
     * JoystickView를 동적으로 생성하고, 설정된 중심 위치에 배치한 후, 루트 레이아웃에 추가합니다.
     */
    private fun createAndAddJoystick(centerX: Float, centerY: Float) {
        // 기존 조이스틱이 있다면 제거
        (binding.root as? FrameLayout)?.removeView(joystickView)

        // 뷰의 크기를 결정 (예: 200dp)
        val sizeDp = 200 // DP 단위
        val sizePx = (sizeDp * resources.displayMetrics.density).toInt()

        // 조이스틱 인스턴스 생성 (여기서는 기본 생성자를 사용)
        joystickView = JoystickView(mContext, null).apply {
            // 조이스틱 움직임 리스너 설정
            setOnMoveListener(object : JoystickView.OnMoveListener {
                override fun onMove(angle: Float, strength: Float) {
                    // 여기서 캐릭터/게임 로직을 적용합니다.
                    Log.d("JoystickFragment", "Angle: $angle, Strength: $strength")
                    joystickSimulator.update(angle, strength)

                    // 예: character.move(angle, strength)
                }
            }, JoystickView.DEFAULT_UPDATE_INTERVAL)

            // LayoutParams 설정
            val params = ViewGroup.LayoutParams(sizePx, sizePx)
            layoutParams = params

            // 초기 위치 설정 (중심 좌표를 기준으로 뷰를 배치)
            x = centerX - sizePx / 2f
            y = centerY - sizePx / 2f

            // 초기 위치 업데이트 (Inner/Outer 원의 위치)
            updatePositionAndCenter(centerX, centerY, centerX, centerY)
        }

        // 루트 레이아웃에 추가
        (binding.root as? FrameLayout)?.addView(joystickView)
    }

    private fun disVisibleCleanBtnNew(){
        uiLayer?.post {
            uiGroupMap.values.forEach { it.visibility = View.GONE }
        }
    }

    private fun makeCleanBtnNew(){

        uiLayer = ConstraintLayout(mContext).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.TRANSPARENT)
        }

        createAndAddGroup("CLEAN_BED", R.drawable.clean_bed1, R.drawable.clean_bed2)
        createAndAddGroup("CLEAN_DESK", R.drawable.clean_desk1, R.drawable.clean_desk2)
        createAndAddGroup("CLEAN_WARDROBE", R.drawable.clean_wardrobe1, R.drawable.clean_wardrobe2)
        createAndAddGroup("CLEAN_WINDOW", R.drawable.clean_window1, R.drawable.clean_window2)

        // 8. (필요하다면) 화면에 UI 레이어 붙이기
        (binding.root as? FrameLayout)?.addView(uiLayer)
    }

    private fun visibleCleanBtnNew(type: GameEvent) {
        // UI 스레드 안전장치
        uiLayer?.post {
            // 1. 일단 모든 그룹을 다 숨김 (초기화)
            uiGroupMap.values.forEach { it.visibility = View.GONE }

            // 2. 원하는 타입만 찾아서 보여줌
            uiGroupMap[type.toString()]?.visibility = View.VISIBLE
        }
    }

    private fun createAndAddGroup(type: String, imgRes1: Int, imgRes2: Int) {

        // 1. LinearLayout(그룹) 생성
        val buttonGroup = LinearLayout(mContext).apply {
            id = View.generateViewId()
            orientation = LinearLayout.HORIZONTAL
            visibility = View.GONE // 기본적으로 숨김 상태로 생성
        }

        // 2. 이미지 생성 및 추가
        val imgParams = LinearLayout.LayoutParams(200, 200).apply {
            leftMargin = 10
            rightMargin = 10
        }

        // 왼쪽 이미지
        buttonGroup.addView(ImageView(mContext).apply {
            setImageResource(imgRes1)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener { Log.d("Game", "$type 왼쪽 클릭") }
        }, imgParams)

        // 오른쪽 이미지
        buttonGroup.addView(ImageView(mContext).apply {
            setImageResource(imgRes2)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener { Log.d("Game", "$type 오른쪽 클릭") }
        }, imgParams)

        // 3. 위치 설정 (중앙 하단)
        val groupParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            verticalBias = 0.8f // 하단 80% 지점
        }

        // 4. 레이어에 추가하고, 나중에 찾을 수 있게 Map에 저장
        uiLayer?.addView(buttonGroup, groupParams)
        uiGroupMap[type] = buttonGroup
    }


    override fun onSelectItem(type: ItemType) {

        if(viewModel.countSelectItem){
            when (type) {
                ItemType.DEFAULT -> TODO()
                ItemType.CLEAN_BED -> {
                    viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.CLEAN_BED))
                }
                ItemType.CLEAN_WINDOW -> {
                    viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.CLEAN_WINDOW))
                }
                ItemType.CLEAN_DESK -> {
                    viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.CLEAN_DESK))
                }
                ItemType.CLEAN_WARDROBE -> {
                    viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.CLEAN_WARDROBE))
                }
            }
            viewModel.countSelectItem = false
        }
//        findNavController().navigate(R.id.WindowFragment)
    }

    override fun onNotSelectItem() {
        viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.DISABLE_CLEAN_BTN))
        viewModel.countSelectItem = true
    }

    override fun onLevelCleared() {
        TODO("Not yet implemented")
    }
}