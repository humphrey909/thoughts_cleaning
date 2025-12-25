package com.example.thoughts_cleaning.views.game.view.fragment

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.CleanStateBtnItem
import com.example.thoughts_cleaning.base.MasilFragment
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.vm.viewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentGameBinding
import com.example.thoughts_cleaning.util.CustomLoadingView
import com.example.thoughts_cleaning.util.dialog.QuestionInputDialog
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.util.ItemType
import com.example.thoughts_cleaning.util.JoystickState
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogBuilder
import com.example.thoughts_cleaning.util.dialog.recent.CommonDialogType
import com.example.thoughts_cleaning.views.game.GameEvent
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.game.vm.fragment.GameFragmentViewModel
import com.three.joystick.JoystickView
import kotlin.collections.set

class GameFragment : MasilFragment<FragmentGameBinding, GameFragmentViewModel>(R.layout.fragment_game), GameView.GameActionListener {

    override val viewModel by viewModelFactory { GameFragmentViewModel() }

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
//    private var buttonGroup: LinearLayout? = null
    private val uiCleanBtnGroupMap = mutableMapOf<String, LinearLayout>() // 청소 버튼 그룹
    private var textAlertTextLinear: LinearLayout? = null //알림 텍스트 창

    private var alertThread: Thread? = null

    var gaugeFillView: View? = null

    var cleanIconView: ImageView? = null

    var backPressedCallback: OnBackPressedCallback? = null

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
            requireActivity() as GameActivity, this, joystickSimulator
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

        val loadingView = CustomLoadingView(requireContext())
        (binding.root as? ViewGroup)?.addView(loadingView)

        gameView.setOnFirstFrameDrawnListener {
            Log.d("DEBUG_TAG", "리스너: 콜백 받음! 로딩뷰 숨김 시도")

            loadingView.post {
                loadingView.visibility = View.GONE
                Log.d("DEBUG_TAG", "리스너: 숨김 처리 완료")
            }
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
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_WINDOW -> {
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_DESK -> {
                        visibleCleanBtnNew(it.moveType)
                    }
                    GameEvent.CLEAN_WARDROBE -> {
                        visibleCleanBtnNew(it.moveType)
                    }

                    GameEvent.DISABLE_CLEAN_BTN -> {
                        disVisibleCleanBtnNew()
                    }

                    GameEvent.GAME_FINISH -> {
                        showDialogFinishOneButton()
                    }
                }
            }
        }

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogFinishTwoButton()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,backPressedCallback!!
        )

        makeCleanBtnNew()
        makeAlert()
        timerUIFunction()

        makeCleanGauge()
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
            uiCleanBtnGroupMap.values.forEach { it.visibility = View.GONE }
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

        var bedImgUrl: Int = 0

        Log.d("currentScale", "CLEAN_BED count: ${viewModel.cleanImgBtnMap["CLEAN_BED"]?.count}") //null 인 이유

        if(viewModel.cleanImgBtnMap["CLEAN_BED"]?.count == 1){
            bedImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img1
        }else if(viewModel.cleanImgBtnMap["CLEAN_BED"]?.count == 2){
            bedImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img2
        }else{
            bedImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img3
        }

        createAndAddGroup("CLEAN_BED", bedImgUrl)

        var deskImgUrl: Int = 0

        if(viewModel.cleanImgBtnMap["CLEAN_DESK"]?.count == 1){
            deskImgUrl = viewModel.cleanImgBtnMap["CLEAN_DESK"]!!.img1
        }else if(viewModel.cleanImgBtnMap["CLEAN_DESK"]?.count == 2){
            deskImgUrl = viewModel.cleanImgBtnMap["CLEAN_DESK"]!!.img2
        }else{
            deskImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img3
        }
        createAndAddGroup("CLEAN_DESK", deskImgUrl)

        var wardrobeImgUrl: Int = 0

        if(viewModel.cleanImgBtnMap["CLEAN_WARDROBE"]?.count == 1){
            wardrobeImgUrl = viewModel.cleanImgBtnMap["CLEAN_WARDROBE"]!!.img1
        }else if(viewModel.cleanImgBtnMap["CLEAN_WARDROBE"]?.count == 2){
            wardrobeImgUrl = viewModel.cleanImgBtnMap["CLEAN_WARDROBE"]!!.img2
        }else{
            wardrobeImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img3
        }
        createAndAddGroup("CLEAN_WARDROBE", wardrobeImgUrl)

        var windowImgUrl: Int = 0

        if(viewModel.cleanImgBtnMap["CLEAN_WINDOW"]?.count == 1){
            windowImgUrl = viewModel.cleanImgBtnMap["CLEAN_WINDOW"]!!.img1
        }else if(viewModel.cleanImgBtnMap["CLEAN_WINDOW"]?.count == 2){
            windowImgUrl = viewModel.cleanImgBtnMap["CLEAN_WINDOW"]!!.img2
        }else{
            windowImgUrl = viewModel.cleanImgBtnMap["CLEAN_BED"]!!.img3
        }
        createAndAddGroup("CLEAN_WINDOW", windowImgUrl)

        (binding.root as? FrameLayout)?.addView(uiLayer)
    }

    private fun visibleCleanBtnNew(type: GameEvent) {
        // UI 스레드 안전장치
        uiLayer?.post {
            // 1. 일단 모든 그룹을 다 숨김 (초기화)
            uiCleanBtnGroupMap.values.forEach { it.visibility = View.GONE }

            // 2. 원하는 타입만 찾아서 보여줌
            uiCleanBtnGroupMap[type.toString()]?.visibility = View.VISIBLE
        }
    }

    private fun createAndAddGroup(type: String, imgRes: Int) {

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

        cleanIconView = ImageView(mContext).apply {
            setImageResource(imgRes)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener {
                responseClick(type)
            }
        }
        buttonGroup.addView(cleanIconView, imgParams)

//        val fillParams = ConstraintLayout.LayoutParams(
//            0,
//            16
//        ).apply {
//            topToTop = gaugeImgId
//            bottomToBottom = gaugeImgId
//            startToStart = gaugeImgId
//            endToEnd = gaugeImgId
//
//            matchConstraintPercentWidth = 0.1f
//            horizontalBias = 0.0f
//
//            marginStart = 7
//        }
//
//        gaugeFillView = View(mContext).apply {
//            id = View.generateViewId()
//            // 위에서 만든 녹색 채움 xml 적용
//            setBackgroundResource(R.drawable.bg_clean_gauge_fill)
//        }
//        CleanGaugeConstraintIn.addView(gaugeFillView, fillParams)

//        // 오른쪽 이미지
//        buttonGroup.addView(ImageView(mContext).apply {
//            setImageResource(imgRes2)
//            scaleType = ImageView.ScaleType.FIT_CENTER
//            setOnClickListener {
//                Log.d("Game", "$type 오른쪽 클릭")
//
//                //type
//                responseClick(type, 2)
//
//            }
//        }, imgParams)

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
        uiCleanBtnGroupMap[type] = buttonGroup
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

    //특정 가구 청소시 순서에 따라 다른 효과가 나타난다.
    //클릭시 아이콘 이미지 변경
    fun responseClick(type: String){
        if(viewModel.cleanImgBtnMap[type]!!.count <  viewModel.countCleanBtn){

            //가구 변경
            gameView.changeCleanFurniture(type, viewModel.cleanImgBtnMap[type]!!.count)


            //청소 버튼 아이콘 변경
            changeCleanIcon(type)

            //청소 퍼센트 게이지 변경
            updateGaugePercent()

            //가구별 청소 카운트 변경
            viewModel.cleanImgBtnMap[type] = viewModel.cleanImgBtnMap[type]!!.copy(count = viewModel.cleanImgBtnMap[type]!!.count+1)
        }
    }

    private fun makeAlert(){

        uiLayer = ConstraintLayout(mContext).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.TRANSPARENT)
        }

        createAlertText()

        // 8. (필요하다면) 화면에 UI 레이어 붙이기
        (binding.root as? FrameLayout)?.addView(uiLayer)
    }

    private fun createAlertText() {

        // 1. LinearLayout(그룹) 생성 (기존과 동일)
        textAlertTextLinear = LinearLayout(mContext).apply {
            id = View.generateViewId()
            orientation = LinearLayout.HORIZONTAL
            visibility = View.GONE
            // 자식 뷰(텍스트뷰)들 사이의 정렬을 위해 gravity 설정 (선택사항)
            gravity = Gravity.CENTER
        }

        // 2. 텍스트뷰를 위한 LayoutParams 설정
        val textParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, // 텍스트 길이에 맞게 늘어남 (고정 크기를 원하면 200, 200 입력)
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = 10
            rightMargin = 10
        }

        // 텍스트 내용
        textAlertTextLinear?.addView(TextView(mContext).apply {
            // [텍스트 설정]
            text = mContext.getString(R.string.dialog_game_text1)
            textSize = 16f
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT

            // [정렬 설정]
            gravity = Gravity.CENTER

            tag = "GAME_ALERT_TEXT"

            // [배경 설정]
            // 1. 단순 색상인 경우:
            // setBackgroundColor(Color.BLUE)
            // 2. 드로어블(둥근 모서리 등)인 경우:
            setBackgroundResource(R.drawable.bg_game_alert_text)

            // [패딩 설정] - 배경과 글자 사이의 여백
            setPadding(30, 20, 30, 20)

//            // [클릭 리스너]
//            setOnClickListener {
//                Log.d("Game", "$type 텍스트 클릭")
//                responseClick(type, 1)
//            }
        }, textParams)

        // 3. 위치 설정 (중앙 하단) - (기존과 동일)
        val groupParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            verticalBias = 0.8f
        }

        // 4. 레이어에 추가
        uiLayer?.addView(textAlertTextLinear, groupParams)
    }

    private fun timerUIFunction() {
        alertThread = Thread {
            try {
                updateAlertText(mContext.getString(R.string.dialog_game_text1))
                visibleAlertText(true)
                // 1. 3초간 대기 (백그라운드 쓰레드에서 실행)
                Thread.sleep(3000)

                visibleAlertText(false)

                //
                Thread.sleep(1000)
                //

                updateAlertText(mContext.getString(R.string.dialog_game_text2))
                visibleAlertText(true)
                // 1. 3초간 대기 (백그라운드 쓰레드에서 실행)
                Thread.sleep(3000)

                visibleAlertText(false)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        alertThread?.start()
    }



    private fun visibleAlertText(type: Boolean) {
        // UI 스레드 안전장치
        uiLayer?.post {
            if(type){
                textAlertTextLinear?.visibility = View.VISIBLE
            }else{
                textAlertTextLinear?.visibility = View.GONE
            }
        }
    }

    fun updateAlertText(newMessage: String) {
        val targetTextView = textAlertTextLinear?.findViewWithTag<TextView>("GAME_ALERT_TEXT")
        (mContext as? Activity)?.runOnUiThread {
            targetTextView?.text = newMessage
        }
    }

    //청소 게이지 생성
    private fun makeCleanGauge(){

        uiLayer = ConstraintLayout(mContext).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.TRANSPARENT)
        }

        createCleanGauge()

        // 8. (필요하다면) 화면에 UI 레이어 붙이기
        (binding.root as? FrameLayout)?.addView(uiLayer)
    }

    private fun createCleanGauge() {

        var CleanGaugeConstraint = ConstraintLayout(mContext).apply {
            id = View.generateViewId()
//            orientation = LinearLayout.HORIZONTAL
            visibility = View.VISIBLE
//            setBackgroundColor(Color.parseColor("#80FFFFFF"))
            setPadding(25, 50, 50, 20)
        }

        val CleanGaugeConstraintParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            0
        ).apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

            matchConstraintPercentHeight = 0.1f
            verticalBias = 0.0f
        }


        //내부 오른쪽 공간
        val CleanGaugeConstraintInParams = ConstraintLayout.LayoutParams(
            0,
            0
        ).apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

            matchConstraintPercentWidth = 0.5f
            horizontalBias = 1.0f
        }

        var CleanGaugeConstraintIn = ConstraintLayout(mContext).apply {
            id = View.generateViewId()
//            orientation = LinearLayout.HORIZONTAL
            visibility = View.VISIBLE
//            setBackgroundColor(Color.parseColor("#80FFFFFF"))
        }
        CleanGaugeConstraint.addView(CleanGaugeConstraintIn, CleanGaugeConstraintInParams)


        // ---------------------------------------------------------
        // 게이지 틀 이미지 추가
        // ---------------------------------------------------------
        val imageParams = ConstraintLayout.LayoutParams(
            0, 30
        ).apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }

        val gaugeImgId = View.generateViewId()
        CleanGaugeConstraintIn?.addView(ImageView(mContext).apply {
            id = gaugeImgId
            setImageResource(R.drawable.bg_clean_gauge)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }, imageParams)



        // ---------------------------------------------------------------
        // 실제 차오르는 게이지 색상 (Fill View)
        // ---------------------------------------------------------------
        val fillParams = ConstraintLayout.LayoutParams(
            0,
            16
        ).apply {
            topToTop = gaugeImgId
            bottomToBottom = gaugeImgId
            startToStart = gaugeImgId
            endToEnd = gaugeImgId

            matchConstraintDefaultWidth = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT
            matchConstraintPercentWidth = 0f
            horizontalBias = 0.0f

            marginStart = 7
//            marginEnd = 7
        }

        gaugeFillView = View(mContext).apply {
            id = View.generateViewId()
            // 위에서 만든 녹색 채움 xml 적용
            setBackgroundResource(R.drawable.bg_clean_gauge_fill)
        }
        CleanGaugeConstraintIn.addView(gaugeFillView, fillParams)




        // ---------------------------------------------------------
        // 게이지 위의 제목
        // ---------------------------------------------------------
        val textParams = ConstraintLayout.LayoutParams(
            0, ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomToTop = gaugeImgId
            startToStart = gaugeImgId
            bottomMargin = 5
        }

        CleanGaugeConstraintIn?.addView(TextView(mContext).apply {
            text = mContext.getString(R.string.title_clean_gauge)
            textSize = 14f
            setTextColor(Color.BLACK)
            typeface = Typeface.DEFAULT
            tag = "TITLE_CLEAN_GAUGE"
        }, textParams)


        // ---------------------------------------------------------
        // 닫기 버튼
        // ---------------------------------------------------------
        val btnCloseParams = ConstraintLayout.LayoutParams(
            60, 60
        ).apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            topMargin = 40
        }

        val btnCloseId = View.generateViewId()
        CleanGaugeConstraint?.addView(ImageView(mContext).apply {
            id = btnCloseId
            setImageResource(R.drawable.btn_close)
            scaleType = ImageView.ScaleType.FIT_CENTER

            setOnClickListener {
                showDialogFinishTwoButton()
            }
        }, btnCloseParams)


        // 4. 레이어에 추가
        uiLayer?.addView(CleanGaugeConstraint, CleanGaugeConstraintParams)
    }


    private fun showDialogFinishTwoButton(){
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.TWO_BUTTON_GAME)
                .title(getString(R.string.dialog_game_title))
                .main(getString(R.string.dialog_game_document))
                .onConfirmListener {
                    backPressedCallback!!.isEnabled = false

                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                .onCancelListener{

                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }
    private fun showDialogFinishOneButton(){
        val dialog = context?.let {
            CommonDialogBuilder(it, CommonDialogType.ONE_BUTTON_GAME)
                .title(getString(R.string.dialog_game_title))
                .main(getString(R.string.dialog_game_document))
                .onConfirmListener {
                    backPressedCallback!!.isEnabled = false

                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                .build()
        }
        if (dialog != null) {
            showDialog(dialog)
        }
    }

    fun changeCleanIcon(type: String) {
        val targetImageView = uiCleanBtnGroupMap[type]!!.getChildAt(0) as? ImageView
        if(viewModel.cleanImgBtnMap[type]!!.count == 1) {
            activity?.runOnUiThread {
                targetImageView?.setImageResource(viewModel.cleanImgBtnMap[type]!!.img2)
            }
        }else if (viewModel.cleanImgBtnMap[type]!!.count == 2){
            activity?.runOnUiThread {
                targetImageView?.setImageResource(viewModel.cleanImgBtnMap[type]!!.img3)
            }
        }
    }

    fun updateGaugePercent() {
        if(viewModel.countClean != viewModel.totalCountClean) {
            viewModel.countClean = viewModel.countClean+1

            val vPercent = 1f / viewModel.totalCountClean
            val vPercentOne = vPercent * viewModel.countClean.toFloat()

            val safePercent = vPercentOne.coerceIn(0f, 1f)

            (mContext as? Activity)?.runOnUiThread {
                val params = gaugeFillView?.layoutParams as? ConstraintLayout.LayoutParams
                params?.let {
                    it.matchConstraintPercentWidth = safePercent
                    gaugeFillView?.layoutParams = it // 변경사항 적용
                }
            }


            //종료 시점
            if(viewModel.countClean == viewModel.totalCountClean){
                viewModel._moveEvent.postValue(MoveEvent.Game(GameEvent.GAME_FINISH))
            }
        }
    }

}