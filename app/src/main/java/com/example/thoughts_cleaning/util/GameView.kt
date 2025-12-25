package com.example.thoughts_cleaning.util

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.game.view.fragment.GameFragment
import android.animation.AnimatorListenerAdapter
import com.example.thoughts_cleaning.R

class GameView(context: Context, val activity: GameActivity, var fragment: GameFragment, private val joystickState: JoystickState) : SurfaceView(context), SurfaceHolder.Callback {

    // 1. 페이지 이동 요청을 위한 인터페이스 정의
    interface GameActionListener {
        fun onSelectItem(type: ItemType)
        fun onNotSelectItem()
        fun onLevelCleared()
    }

    private var listener: GameActionListener? = null
    var onFirstFrameDrawn: (() -> Unit)? = null

    @Volatile
    var isFrameAlreadyDrawn = false

    private lateinit var gameThread: GameThread
    private val uiHandler = Handler(Looper.getMainLooper())

    // 1. 현재 화면 상태 변수 (애니메이션에 의해 변경됨)
     var currentScale = 1.0f
    var currentPanX = 0f
    var currentPanY = 0f

    // 2. 확대할 목표 지점 (줌인 대상 물체의 중심 좌표)
    var targetFocusX = 0f
    var targetFocusY = 0f
    private val zoomTargetScale = 1.5f // 3배 확대 목표

    // 애니메이션 객체
    private var animator: ValueAnimator? = null

    var gameStateDirection: GameState.GameStateFlow = GameState.GameStateFlow.COMMON // RUNNING, ZOOMING, CLEANING_MODE

    init {
        holder.addCallback(this)
        // 화면이 포커스를 받을 수 있도록 설정
        isFocusable = true
    }

    fun setOnFirstFrameDrawnListener(listener: () -> Unit) {
        this.onFirstFrameDrawn = listener

        if (isFrameAlreadyDrawn) {
            listener()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Surface가 생성되면 스레드를 시작합니다.
        gameThread = GameThread(holder, context, activity, fragment, joystickState, this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // 화면 크기가 변경될 때 처리 (필요시)


    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface가 파괴되기 전에 스레드를 안전하게 종료합니다.
        var retry = true
        gameThread.isRunning = false
        while (retry) {
            try {
                gameThread.join()
                retry = false
            } catch (e: InterruptedException) {
                // 스레드 종료 대기 중 인터럽트 발생 시 재시도
            }
        }
    }

    fun pause() {
        if (::gameThread.isInitialized) {
            gameThread.isRunning = false
        }
    }

    fun resume() {
        if (::gameThread.isInitialized) {
            gameThread.isRunning = true
            // 새로운 스레드를 시작하거나 재개 로직이 필요할 수 있습니다.
            // 여기서는 단순화하여 run()에서 while(isRunning)만 제어합니다.
        }
    }


//    fun startZoomInAnimation(objectCenterX: Float, objectCenterY: Float) {
//        uiHandler.post {
//            // ValueAnimator 시작 코드를 여기에 넣습니다.
//            // ... animator.start() ...
//
//            gameThread.startZoomInAnimation(objectCenterX, objectCenterY)
//        }
//    }

    fun startZoomInAnimation(objectCenterX: Float, objectCenterY: Float, screenWidth: Int, screenHeight: Int) {
        targetFocusX = objectCenterX
        targetFocusY = objectCenterY

        // 뷰 중앙에 목표 아이템이 오도록 최종 Pan 목표 계산
        val targetPanX = (screenWidth / 2f) - (targetFocusX * zoomTargetScale)
        val targetPanY = (screenHeight / 2f) - (targetFocusY * zoomTargetScale)

        // ValueAnimator를 사용하여 currentScale, currentPanX/Y 값을 부드럽게 변경
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 5000L

            addUpdateListener {
                val fraction = it.animatedValue as Float

                currentScale = 1.0f + (zoomTargetScale - 1.0f) * fraction

                Log.d("currentScale", "currentScale: $currentScale")

                currentPanX = targetPanX * fraction
                currentPanY = targetPanY * fraction

                Log.d("currentScale", "currentPanX: $currentPanX")
                Log.d("currentScale", "currentPanY: $currentPanY")

                // SurfaceView는 invalidate() 대신 렌더링 스레드가 다음 프레임을 그리도록 합니다.
                // (대부분의 SurfaceView 게임 루프는 자동으로 화면을 계속 갱신합니다.)
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // 애니메이션 종료 후 청소 모드로 전환
                    gameStateDirection = GameState.GameStateFlow.CLEANING_MODE


                    // startCleaningMode() 호출
                }
            })

            start()
        }
    }

    // 3. 리스너 설정 함수
    fun setGameActionListener(listener: GameActionListener) {
        this.listener = listener
    }

    // 청소 버튼 활성화
    fun startWorkCleanBtn(type: ItemType) {
        // UI 작업을 위해 메인 스레드에서 실행하도록 요청
        handler.post {
            listener?.onSelectItem(type)
        }
    }

    // 청소 버튼 비활성화
    fun stopWorkCleanBtn() {
        // UI 작업을 위해 메인 스레드에서 실행하도록 요청
        handler.post {
            listener?.onNotSelectItem()
        }
    }

    fun changeCleanFurniture(furniture: String, type: Int) {
        when(furniture){
            "BED" -> {
                if(type == 1){ // 첫번째 버튼
                    //침대 이미지 변경
                    gameThread.fixBedLocation(R.drawable.room_structure_bed_dirty_stage1)
                }else{ // 두번째 버튼
                    gameThread.fixBedLocation(R.drawable.room_structure_bed_clean)
                }
            }
            "DESK" -> {
                if(type == 1){ // 첫번째 버튼
                    gameThread.fixDeskLocation(R.drawable.room_structure_desk_dirty_stage1)
                }else{ // 두번째 버튼
                    gameThread.fixDeskLocation(R.drawable.room_structure_desk_clean)
                }
            }
            "WARDROBE" -> {
                if(type == 1){ // 첫번째 버튼
                    gameThread.fixWardrobeLocation(R.drawable.room_structure_wardrobe_dirty_stage1)
                }else{ // 두번째 버튼
                    gameThread.fixWardrobeLocation(R.drawable.room_structure_wardrobe_clean)
                }
            }
            "WINDOW" -> {
                if(type == 1){ // 첫번째 버튼
                    gameThread.fixSmallWindowLocation(R.drawable.room_structure_small_window_dirty_stage1)
                }else{ // 두번째 버튼
                    gameThread.fixSmallWindowLocation(R.drawable.room_structure_small_window_clean)
                }
            }
        }
    }
}