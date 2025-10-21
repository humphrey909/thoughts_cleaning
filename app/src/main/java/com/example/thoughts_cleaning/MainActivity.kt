package com.example.thoughts_cleaning

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.window.layout.WindowMetricsCalculator
import com.example.thoughts_cleaning.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

//    private lateinit var joystickView: JoyStickView
    private var isStop = false

    private var screenWidth = 0
    private var screenHeight = 0

    private var MOVE_FACTOR = 0

    private var prevAngle = 0
    private lateinit var prevImageResource:LiveData<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)

        // Set up data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Bind the ViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 1. WindowMetricsCalculator 인스턴스 가져오기
        val windowMetricsCalculator = WindowMetricsCalculator.getOrCreate()

        // 2. 현재 창(Activity)의 WindowMetrics 계산
        val metrics = windowMetricsCalculator.computeCurrentWindowMetrics(this)

        // 3. 높이(height)와 너비(width) 구하기
        screenHeight = metrics.bounds.height()
        screenWidth = metrics.bounds.width()

        Log.d("ScreenSize", "화면 높이: $screenHeight px")
        Log.d("ScreenSize", "화면 너비: $screenWidth px")

        setJoystick()



        viewModel.characterImages.observe(this) { imageResId ->
            // LiveData가 변경(새로운 Int ID)될 때마다 ImageView의 이미지를 설정
            binding.charImageView.setImageResource(imageResId)
        }
    }





//    /** 조이스틱 버튼 설정하는 함수 */
//    private fun setJoystick() {
////        val angleValueView = findViewById<TextView>(R.id.value_angle)
////        val strengthValueView = findViewById<TextView>(R.id.value_strength)
//
//
////        val joystickView = findViewById<JoyStickView>(R.id.joystick)
//        binding.joystick.setOnMoveListener({ angle, strength ->
//            Log.d("JoystickDebug", "Moved! Angle: $angle, Strength: $strength")
//
//
//            binding.valueAngle.text = "angle : ${angle}"; binding.valueStrength.text = "strength : ${strength}"
//        }, 100)
//    }








    override fun onResume() {
        super.onResume()
        isStop = false
        binding.joystick.setAngle(0)
        binding.joystick.setStrength(0)
        binding.joystick.setOnMoveListener { angle, strength ->
            if (!isStop) {
                updateCharacterImage(angle, strength, binding.charImageView, viewModel.characterImages)
                moveCharacter(angle, strength, screenWidth, screenHeight, binding.charImageView)
//                checkCoinIntersect(binding.charImageView)
//                checkMonsterIntersect(binding.charImageView)
            }
        }
    }

    /** 조이스틱 버튼 설정하는 함수 */
    private fun setJoystick() {
        binding.joystick.setOnMoveListener { angle, strength ->
            if (!isStop) {
                updateCharacterImage(angle, strength, binding.charImageView, viewModel.characterImages)
                moveCharacter(angle, strength, screenWidth, screenHeight, binding.charImageView)
//                checkMonsterIntersect(binding.charImageView)
//                checkCoinIntersect(binding.charImageView)
            }
        }
    }

    /** 조이스틱 각도에 따라 캐릭터 이미지 변경하는 함수 */
    private fun updateCharacterImage(
        angle: Int,
        strength: Int,
        charImageView: ImageView,
        userImages: LiveData<Int>
    ) {
        val imageResource = userImages

//        val imageResource = when {
//            (angle >= 315 || angle < 45) -> if (strength == 0) userImages[9] else getWalkingImage(
//                userImages[10],
//                userImages[11]
//            )
//
//            (angle >= 45 && angle < 135) -> if (strength == 0) userImages[0] else getWalkingImage(
//                userImages[1],
//                userImages[2]
//            )
//
//            (angle >= 135 && angle < 225) -> if (strength == 0) userImages[6] else getWalkingImage(
//                userImages[7],
//                userImages[8]
//            )
//
//            (angle >= 225 && angle < 315) -> if (strength == 0) userImages[3] else getWalkingImage(
//                userImages[4],
//                userImages[5]
//            )
//
//            else -> prevImageResource
//        }

        // 조이스틱 멈췄을 때 이전 상태의 기본 이미지 유지
        if (strength == 0) {
            loadImageFromUrl(getPreviousDirectionImage(prevAngle, userImages), charImageView)
        } else {
            loadImageFromUrl(imageResource, charImageView)
            prevAngle = angle
            prevImageResource = imageResource
        }
    }

    private fun loadImageFromUrl(previousDirectionImage: LiveData<Int>, charImageView: ImageView) {

    }

    /** 캐릭터 걷는 동작 설정하는 함수 */
//    private fun getWalkingImage(image1: String, image2: String): String {
//        // 번갈아 가며 이미지 반환
//        walkingCounter = (walkingCounter + 1) % WALKING_SPEED
//        return if (walkingCounter < 5) image1 else image2
//    }

    /** 이전 상태 방향의 기본 이미지 반환하는 함수 */
    private fun getPreviousDirectionImage(angle: Int, userImages: LiveData<Int>): LiveData<Int> {

//        val direction = when {
//            (angle >= 315 || angle < 45) -> userImages[9]
//            (angle >= 45 && angle < 135) -> userImages[0]
//            (angle >= 135 && angle < 225) -> userImages[6]
//            (angle >= 225 && angle < 315) -> userImages[3]
//            else -> userImages[3]
//        }

        val direction = when {
            (angle >= 315 || angle < 45) -> userImages
            (angle >= 45 && angle < 135) -> userImages
            (angle >= 135 && angle < 225) -> userImages
            (angle >= 225 && angle < 315) -> userImages
            else -> userImages
        }
        return direction
    }

    /** 캐릭터 움직이는 함수 */
    private fun moveCharacter(
        angle: Int,
        strength: Int,
        screenWidth: Int,
        screenHeight: Int,
        charImageView: ImageView
    ) {
        // 조이스틱의 각도를 라디안으로 변환
        val radian = Math.toRadians(angle.toDouble())

        // 조이스틱의 각도에 맞게 x, y 방향을 계산
        val moveX = strength * MOVE_FACTOR * Math.cos(radian).toFloat()
        val moveY = strength * MOVE_FACTOR * -Math.sin(radian).toFloat()

        // 현재 캐릭터의 위치
        val currentX = charImageView.x
        val currentY = charImageView.y

        // 이동 후 위치 계산
        val newX = currentX + moveX
        val newY = currentY + moveY

        // 화면 경계 내에 위치하도록 제한
        val clampedX = newX.coerceIn(0f, (screenWidth - charImageView.width).toFloat())
        val clampedY = newY.coerceIn(0f, (screenHeight - charImageView.height).toFloat())

        // "charImageView" 이미지 뷰의 위치 변경
        charImageView.x = clampedX
        charImageView.y = clampedY

        Log.d("moveCharacter", radian.toString())
        Log.d("moveCharacter", moveX.toString())
        Log.d("moveCharacter", moveY.toString())
        Log.d("moveCharacter", currentX.toString())
        Log.d("moveCharacter", currentY.toString())
        Log.d("moveCharacter", newX.toString())
        Log.d("moveCharacter", newY.toString())
        Log.d("moveCharacter", clampedX.toString())
        Log.d("moveCharacter", clampedY.toString())

    }

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
}