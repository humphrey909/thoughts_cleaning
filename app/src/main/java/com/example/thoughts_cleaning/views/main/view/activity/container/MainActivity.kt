package com.example.thoughts_cleaning.views.main.view.activity.container

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.thoughts_cleaning.views.main.vm.activity.container.MainActivityViewModel
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.databinding.ActivityMainBinding
import com.example.thoughts_cleaning.util.GameView
import com.example.thoughts_cleaning.util.JoystickState
import com.three.joystick.JoystickView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

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

    private lateinit var gameView: GameView
    private val joystickSimulator = JoystickState()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // 3. 바인딩 객체의 root 뷰를 Activity의 컨텐츠 뷰로 설정
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as? NavHostFragment

        if (navHostFragment != null) {
            val navController = navHostFragment.navController

            // 4. BottomNavigationView와 NavController 연결
            // R.id.nav_view는 activity_main.xml에 있는 BottomNavigationView의 ID라고 가정합니다.
            binding.bottomNavInFragment.setupWithNavController(navController)
        } else {
            // 오류 처리: NavHostFragment를 찾을 수 없을 때의 로직
            // Log.e("MainActivity", "NavHostFragment not found!")
        }

        binding.bottomNavInFragment.itemIconTintList = null
    }




    override fun onResume() {
        super.onResume()

        //이부분 주석처리
//        val containerLayout = FrameLayout(this)
//        gameView = GameView(this, this, joystickSimulator)
//        val params = FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.MATCH_PARENT,
//            FrameLayout.LayoutParams.MATCH_PARENT
//        )
//        containerLayout.addView(gameView, params)
//
//        setContentView(containerLayout)
//
//
//
//        // 조이스틱 인스턴스 생성 및 레이아웃 설정
//        val joystickView = JoystickView(this, null).apply {
//            layoutParams = FrameLayout.LayoutParams(300, 300).apply {
//                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
//                setMargins(50, 50, 50, 50) // 하단 여백
//            }
//
//            // 💡 참고:
//            // setBackgroundColor(0x88AAAAAA.toInt()) 대신
//            // XML 속성(joystickOuterColor, joystickInnerColor)의 기본값이 적용됩니다.
//            // 만약 코드로 색상을 변경하고 싶다면, JoystickView 내부에 public setter를 추가해야 합니다.
//        }
//
//        // 컨테이너 레이아웃에 조이스틱 뷰 추가
//        containerLayout.addView(joystickView)
//
//        // 조이스틱 움직임 감지 리스너 설정
//        // 기존의 handleJoystickTouch() 대신 조이스틱의 onMove() 인터페이스를 사용합니다.
//        joystickView.setOnMoveListener(
//            object : JoystickView.OnMoveListener {
//                override fun onMove(angle: Float, strength: Float) {
//                    // angle: 0~360도의 각도
//                    // strength: 0~100%의 강도
//                    // 여기에 실제 조이스틱 움직임에 따른 로직을 구현합니다.
//                    // 예를 들어:
//                    // Log.d("Joystick", "Angle: $angle, Strength: $strength")
//
//                    // (기존의 handleJoystickTouch가 하던 조이스틱 입력 시뮬레이션 역할을 이 부분이 대체합니다.)
//                    joystickSimulator.update(angle, strength)
//
//                }
//            },
//            JoystickView.DEFAULT_UPDATE_INTERVAL // 주기적 업데이트 간격 (예: 50ms)
//        )






        // 3. 임시 조이스틱 역할 뷰 추가 (화면 하단 중앙에 배치 가정)
//        val joystickPlaceholder = android.view.View(this).apply {
//            layoutParams = FrameLayout.LayoutParams(300, 300).apply {
//                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
//                setMargins(50, 50, 50, 50) // 하단 여백
//            }
//            setBackgroundColor(0x88AAAAAA.toInt()) // 투명한 회색 배경
//        }
//        containerLayout.addView(joystickPlaceholder)
//
//        // 4. 터치 이벤트 리스너: 터치 좌표를 조이스틱 입력으로 시뮬레이션
//        joystickPlaceholder.setOnTouchListener { v, event ->
//            handleJoystickTouch(v, event)
//            true
//        }




//        isStop = false
//        binding.joystick.setAngle(0)
//        binding.joystick.setStrength(0)
//
//        if (::gameView.isInitialized) gameView.resume()
//
//        binding.joystick.setOnMoveListener { angle, strength ->
//            if (!isStop) {
////                updateCharacterImage(angle, strength, binding.charImageView, viewModel.characterImages)
////                moveCharacter(angle, strength, screenWidth, screenHeight, binding.charImageView)
////                checkCoinIntersect(binding.charImageView)
////                checkMonsterIntersect(binding.charImageView)
//            }
//        }
        //이부분 주석처리

    }
    // 조이스틱 역할을 시뮬레이션하는 터치 핸들러
    /*   private fun handleJoystickTouch(view: View, event: MotionEvent) {
           val centerX = view.width / 2f
           val centerY = view.height / 2f

           val x = event.x - centerX
           val y = event.y - centerY // 화면 Y축과 일치시키기 위해 -Y를 사용하지 않음

           val strength = Math.sqrt((x * x + y * y).toDouble()).coerceAtMost(centerX.toDouble()).toFloat()
           val angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble())).toFloat()

           when (event.action) {
               MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                   // 각도와 강도를 게임 스레드로 전달할 JoystickState에 업데이트
                   joystickSimulator.update(angle, strength)
               }
               MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                   // 손을 떼면 힘을 0으로 설정하여 캐릭터 정지
                   joystickSimulator.update(0f, 0f)
               }
           }
       }*/


    override fun onPause() {
        if (::gameView.isInitialized) gameView.pause()
        super.onPause()
    }

    /** 조이스틱 버튼 설정하는 함수 */
//    private fun setJoystick() {
//        binding.joystick.setOnMoveListener { angle, strength ->
//            if (!isStop) {
//
//                joystickSimulator.update(angle, strength)
//
//
////                updateCharacterImage(angle, strength, binding.charImageView, viewModel.characterImages)
////                moveCharacter(angle, strength, screenWidth, screenHeight, binding.charImageView)
////                checkMonsterIntersect(binding.charImageView)
////                checkCoinIntersect(binding.charImageView)
//            }
//        }
//    }

    /** 조이스틱 각도에 따라 캐릭터 이미지 변경하는 함수 */
//    private fun updateCharacterImage(
//        angle: Int,
//        strength: Int,
//        charImageView: ImageView,
//        userImages: LiveData<Int>
//    ) {
//        val imageResource = userImages
//
////        val imageResource = when {
////            (angle >= 315 || angle < 45) -> if (strength == 0) userImages[9] else getWalkingImage(
////                userImages[10],
////                userImages[11]
////            )
////
////            (angle >= 45 && angle < 135) -> if (strength == 0) userImages[0] else getWalkingImage(
////                userImages[1],
////                userImages[2]
////            )
////
////            (angle >= 135 && angle < 225) -> if (strength == 0) userImages[6] else getWalkingImage(
////                userImages[7],
////                userImages[8]
////            )
////
////            (angle >= 225 && angle < 315) -> if (strength == 0) userImages[3] else getWalkingImage(
////                userImages[4],
////                userImages[5]
////            )
////
////            else -> prevImageResource
////        }
//
//        // 조이스틱 멈췄을 때 이전 상태의 기본 이미지 유지
//        if (strength == 0) {
//            loadImageFromUrl(getPreviousDirectionImage(prevAngle, userImages), charImageView)
//        } else {
//            loadImageFromUrl(imageResource, charImageView)
//            prevAngle = angle
//            prevImageResource = imageResource
//        }
//    }

//    private fun loadImageFromUrl(previousDirectionImage: LiveData<Int>, charImageView: ImageView) {
//
//    }
//
//    /** 캐릭터 걷는 동작 설정하는 함수 */
////    private fun getWalkingImage(image1: String, image2: String): String {
////        // 번갈아 가며 이미지 반환
////        walkingCounter = (walkingCounter + 1) % WALKING_SPEED
////        return if (walkingCounter < 5) image1 else image2
////    }
//
//    /** 이전 상태 방향의 기본 이미지 반환하는 함수 */
//    private fun getPreviousDirectionImage(angle: Int, userImages: LiveData<Int>): LiveData<Int> {
//
////        val direction = when {
////            (angle >= 315 || angle < 45) -> userImages[9]
////            (angle >= 45 && angle < 135) -> userImages[0]
////            (angle >= 135 && angle < 225) -> userImages[6]
////            (angle >= 225 && angle < 315) -> userImages[3]
////            else -> userImages[3]
////        }
//
//        val direction = when {
//            (angle >= 315 || angle < 45) -> userImages
//            (angle >= 45 && angle < 135) -> userImages
//            (angle >= 135 && angle < 225) -> userImages
//            (angle >= 225 && angle < 315) -> userImages
//            else -> userImages
//        }
//        return direction
//    }
//
//    /** 캐릭터 움직이는 함수 */
//    private fun moveCharacter(
//        angle: Int,
//        strength: Int,
//        screenWidth: Int,
//        screenHeight: Int,
//        charImageView: ImageView
//    ) {
//        // 조이스틱의 각도를 라디안으로 변환
//        val radian = Math.toRadians(angle.toDouble())
//
//        // 조이스틱의 각도에 맞게 x, y 방향을 계산
//        val moveX = strength * MOVE_FACTOR * Math.cos(radian).toFloat()
//        val moveY = strength * MOVE_FACTOR * -Math.sin(radian).toFloat()
//
//        // 현재 캐릭터의 위치
//        val currentX = charImageView.x
//        val currentY = charImageView.y
//
//        // 이동 후 위치 계산
//        val newX = currentX + moveX
//        val newY = currentY + moveY
//
//
//        Log.d("ScreenSize", "화면 높이 2: $screenHeight px")
//        Log.d("ScreenSize", "화면 너비 2: $screenWidth px")
//
//
//        // 화면 경계 내에 위치하도록 제한
//        val clampedX = newX.coerceIn(0f, (screenWidth - charImageView.width).toFloat())
//        val clampedY = newY.coerceIn(0f, (screenHeight - charImageView.height).toFloat())
//
//        // "charImageView" 이미지 뷰의 위치 변경
//        charImageView.x = clampedX
//        charImageView.y = clampedY
//
////        Log.d("moveCharacter", radian.toString())
////        Log.d("moveCharacter", moveX.toString())
////        Log.d("moveCharacter", moveY.toString())
////        Log.d("moveCharacter", currentX.toString())
////        Log.d("moveCharacter", currentY.toString())
////        Log.d("moveCharacter", newX.toString())
////        Log.d("moveCharacter", newY.toString())
////        Log.d("moveCharacter", clampedX.toString())
////        Log.d("moveCharacter", clampedY.toString())
//
//
////        Log.d("moveCharacter1", moveX.toString())
////        Log.d("moveCharacter1", moveY.toString())
////
////
////        Log.d("moveCharacter2", currentX.toString())
////        Log.d("moveCharacter2", currentY.toString())
////
////        Log.d("moveCharacter3", newX.toString())
////        Log.d("moveCharacter3", newY.toString())
//
//    }

    /** 몬스터와 만났을 때 처리 담당하는 함수 */
//    private fun checkMonsterIntersect(charImageView: ImageView) {
//        for (monsterView in monsterViews) {
//            val charRect = Rect()
//            val monsterRect = Rect()
//
//            charImageView.getHitRect(charRect)
//            monsterView.getHitRect(monsterRect)
//
//            if (Rect.intersects(charRect, monsterRect)) {
//                isStop = true
//                binding.joystick.setAngle(0)
//                binding.joystick.setStrength(0)
//                loadImageFromUrl(
//                    getPreviousDirectionImage(prevAngle, gameModel.characterImages),
//                    charImageView
//                )
//
//                val monsterId = monsterView.tag as String
//                val intent = Intent(this, GameDetailActivity::class.java).apply {
//                    putExtra("MONSTER_ID", monsterId)
//                    putExtra("USER_ID", gameModel.userId)
//                }
//                startActivity(intent)
//
//                gameViewModel.handleTicketIntersect(gameModel)
//                setTicketAmount(gameModel.ticketAmount)
//                monsterView.visibility = View.GONE
//                monsterViews.remove(monsterView)
//                break
//            }
//        }
//    }

    /** 코인과 만났을 때 처리 담당하는 함수 */
//    private fun checkCoinIntersect(charImageView: ImageView) {
//        for (coinView in coinViews) {
//            val charRect = Rect()
//            val coinRect = Rect()
//
//            charImageView.getHitRect(charRect)
//            coinView.getHitRect(coinRect)
//
//            if (Rect.intersects(charRect, coinRect)) {
//                gameViewModel.handleCoinIntersect(gameModel)
//                setCoinAmount(gameModel.coinAmount)
//                coinView.visibility = View.GONE
//                coinViews.remove(coinView)
//                break
//            }
//        }
//    }
//}

//     fun showCustomDialog() {
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
//            val dialog = QuestionInputDialog.newInstance(nextQuestion)
//            dialog.show(supportFragmentManager, "QuestionDialog")
//
//        } else {
//            // 모든 질문을 다 소진했을 때의 처리
//            Toast.makeText(this, "모든 질문을 완료했습니다!", Toast.LENGTH_LONG).show()
//        }
//     }
}