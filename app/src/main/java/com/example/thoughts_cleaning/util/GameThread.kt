package com.example.thoughts_cleaning.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.SurfaceHolder
import androidx.window.layout.WindowMetricsCalculator
import android.graphics.Paint

class GameThread(
    private val surfaceHolder: SurfaceHolder,
    context: Context,
    val activity: Activity,
    private val joystickState: JoystickState
) : Thread() {

    @Volatile var isRunning = true
    private val FPS = 60 // 초당 프레임 수
    private val TIME_PER_FRAME = (1000 / FPS).toLong() // 프레임당 밀리초

    // 캐릭터 비트맵 (실제 이미지 리소스로 교체 필요)
    private val characterBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, android.R.drawable.star_on)

    // 캐릭터 현재 위치
    private var charX: Float = 100f
    private var charY: Float = 100f

    private val MOVE_SPEED = 5f // 초당 60프레임 기준 5픽셀씩 이동

    private var screenWidth = 0
    private var screenHeight = 0
    private val gameState = GameState(screenWidth, screenHeight)

    private var lastSpawnTime = System.currentTimeMillis()
    private val spawnInterval = 3000 // 3초마다 아이템 생성

    // GameThread 클래스 내부 (또는 Draw를 담당하는 클래스)
    private val defaultItemPaint = Paint().apply {
        color = Color.GREEN // 일반 아이템 (DEFAULT) 색상
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val speedBoostItemPaint = Paint().apply {
        color = Color.YELLOW // 스피드 부스트 아이템 색상
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val healthPackItemPaint = Paint().apply {
        color = Color.RED // 헬스 팩 아이템 색상
        style = Paint.Style.FILL
        isAntiAlias = true
    }


    override fun run() {

        // 1. WindowMetricsCalculator 인스턴스 가져오기
        val windowMetricsCalculator = WindowMetricsCalculator.getOrCreate()

        // 2. 현재 창(Activity)의 WindowMetrics 계산
        val metrics = windowMetricsCalculator.computeCurrentWindowMetrics(activity)

        // 3. 높이(height)와 너비(width) 구하기
        screenHeight = metrics.bounds.height()
        screenWidth = metrics.bounds.width()

        Log.d("ScreenSize", "화면 높이: $screenHeight px")
        Log.d("ScreenSize", "화면 너비: $screenWidth px")


        var startTime: Long
        var timeMillis: Long
        var waitTime: Long

        while (isRunning) {
            Log.d("canvas", "각도 11: ${joystickState.angle} px")
            Log.d("canvas", "각도 12: ${joystickState.strength} px")

            startTime = System.currentTimeMillis()
            var canvas: Canvas? = null

            try {
                Log.d("canvas", "각도 1: ${joystickState.angle} px")
                Log.d("canvas", "각도 2: ${joystickState.strength} px")


                // 1. 입력 및 업데이트 (Update Logic)
                updateGame()

                // 2. 렌더링 (Draw Logic)
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    if (canvas != null) {
                        drawGame(canvas)
                        drawItems(canvas)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            // 3. 프레임 속도 제어
            timeMillis = System.currentTimeMillis() - startTime
            waitTime = TIME_PER_FRAME - timeMillis



            // 2. 아이템 생성 로직
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastSpawnTime > spawnInterval) {
                gameState.spawnItem(screenWidth, screenHeight)
                lastSpawnTime = currentTime
            }

            // 3. 아이템 충돌 감지 및 처리
            checkItemCollisions()

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    /**
     * 조이스틱 입력에 따라 캐릭터 위치를 업데이트합니다.
     */
    private fun updateGame() {
        val strength = joystickState.strength
        val angle = joystickState.angle

        // 강도가 0보다 클 때만 움직임 처리
        if (strength.compareTo(0f) > 0) {
            // 조이스틱의 강도를 기반으로 이동 속도를 조절
            val speedFactor = strength / 100f
            val moveAmount = MOVE_SPEED * speedFactor

            // 각도를 라디안으로 변환
            val radian = Math.toRadians(angle.toDouble())

            // 각도에 따른 X, Y 이동량 계산
            // Note: 조이스틱 각도 라이브러리에 따라 Y축 방향이 다를 수 있습니다.
            // 표준 수학 좌표계(0도=오른쪽, 90도=위쪽)를 가정합니다.
            val moveX = moveAmount * Math.cos(radian).toFloat()
            val moveY = moveAmount * Math.sin(radian).toFloat()

            // 위치 업데이트
            charX += moveX
            charY += moveY

            // TODO: 화면 경계 내로 charX, charY 클램핑 로직 추가
        }
    }

    /**
     * Canvas에 캐릭터 비트맵을 그립니다.
     */
    private fun drawGame(canvas: Canvas) {
        // 배경을 지웁니다.
        canvas.drawColor(Color.rgb(10, 50, 80))

        // 캐릭터를 현재 위치에 그립니다.
        // drawBitmap(비트맵, 그릴 X 좌표, 그릴 Y 좌표, Paint 객체)
        canvas.drawBitmap(characterBitmap, charX, charY, null)
    }

    /**
     * 플레이어와 모든 아이템 간의 충돌을 확인하고 처리합니다.
     */
    private fun checkItemCollisions() {
        val playerBounds = gameState.player.getBounds()

        // 충돌이 감지된 아이템을 임시로 저장할 리스트
        val itemsToRemove = mutableListOf<Item>()


//        Log.d("Joystick", "items: ${gameState.items}")

        for (item in gameState.items) {
            if (item.getBounds().intersects(playerBounds.left, playerBounds.top, playerBounds.right, playerBounds.bottom)) {

                Log.d("Joystick", "items: ${gameState.items}")


                // 충돌 발생! 아이템 획득 로직 실행
                applyItemEffect(item)
                itemsToRemove.add(item)
            }
        }

        // 충돌된 아이템들을 리스트에서 제거합니다.
        gameState.items.removeAll(itemsToRemove)
    }

    /**
     * 아이템 종류에 따라 플레이어에게 효과를 적용합니다.
     */
    private fun applyItemEffect(item: Item) {
        when (item.type) {
            ItemType.DEFAULT -> {
                // Log.d(TAG, "일반 아이템 획득!")
                gameState.player.health += 10 // 예: 점수 획득 또는 체력 약간 회복
            }
            ItemType.SPEED_BOOST -> {
                // Log.d(TAG, "스피드 부스트 획득!")
                // 예: 플레이어 이동 속도를 잠시 증가시키는 로직
            }
            ItemType.HEALTH_PACK -> {
                // Log.d(TAG, "체력 팩 획득!")
                gameState.player.health = minOf(100, gameState.player.health + 50)
            }
        }
    }

    /**
     * 아이템 리스트를 순회하며 각 아이템을 Canvas에 그립니다.
     */
    private fun drawItems(canvas: Canvas) {
        for (item in gameState.items) {
            val paint = when (item.type) {
                ItemType.DEFAULT -> defaultItemPaint
                ItemType.SPEED_BOOST -> speedBoostItemPaint
                ItemType.HEALTH_PACK -> healthPackItemPaint
            }

            // 아이템을 원형으로 그립니다.
            canvas.drawCircle(item.x, item.y, item.radius, paint)
        }
    }
}