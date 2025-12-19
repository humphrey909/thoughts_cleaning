package com.example.thoughts_cleaning.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.SurfaceHolder
import androidx.window.layout.WindowMetricsCalculator
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.GameWall
import com.example.thoughts_cleaning.views.game.view.activity.container.GameActivity
import com.example.thoughts_cleaning.views.game.view.fragment.GameFragment
import androidx.core.graphics.withSave
import com.example.thoughts_cleaning.MainApplication.Companion.TAG


class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val context: Context,
    val activity: GameActivity,
    val fragment: GameFragment,
    private val joystickState: JoystickState,
    private val wasteCount : Int,
    val gameView: GameView
) : Thread() {

    var canvas: Canvas? = null

//    val desiredWidth: Int = 100 // 원하는 가로 픽셀 크기
//    val desiredHeight: Int = 100 // 원하는 세로 픽셀 크기

    @Volatile var isRunning = true
    private val FPS = 60 // 초당 프레임 수
    private val TIME_PER_FRAME = (1000 / FPS).toLong() // 프레임당 밀리초

    private var accessItemMake = true

//    val crashDifferenceLeft = 132f
//    val crashDifferenceTop = 140f
//    val crashDifferenceRight = 108f
//    val crashDifferenceBottom = 100f

    // 캐릭터 비트맵 (실제 이미지 리소스로 교체 필요)
//    private val characterBitmap: Bitmap =
//        BitmapFactory.decodeResource(context.resources, R.drawable.character_default)
    private val originalBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.character_pose1)

//        private var characterBitmap: Bitmap? = null

    private lateinit var characterRect:RectF

    var aspectRatioCharacter = 0f
    var newWidthCharacter = 0f
    var newHeightCharacter = 0f


    //침대
    private val bedBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.room_structure_bed3)
    private lateinit var bedRect:RectF

    //창문
    private val smallWindowBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.room_structure_small_window2)
    private lateinit var smallWindowRect:RectF

    //책상
    private val deskBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.room_structure_desk2)
    private lateinit var deskRect:RectF

    //옷장
    private val wardrobeBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.room_structure_wardrobe2)
    private lateinit var wardrobeRect:RectF

    private val MOVE_SPEED = 5f // 초당 60프레임 기준 5픽셀씩 이동

    private var screenWidth = 0
    private var screenHeight = 0
    private val gameState = GameState(screenWidth, screenHeight)

    private var lastSpawnTime = System.currentTimeMillis()
    private val spawnInterval = 3000 // 3초마다 아이템 생성
    private val spawnIntervalUntil = 5 // 3초마다 아이템 생성
    private var spawnIntervalSwitch = true // 3초마다 아이템 생성

//    var k = 1

//    var nearestWall: GameWall? = null


    // GameThread 클래스 내부 (또는 Draw를 담당하는 클래스)
    private val defaultItemPaint = Paint().apply {
        color = Color.rgb(182, 57, 57) // 일반 아이템 (DEFAULT) 색상
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

    private var preRenderedBackgroundBitmap: Bitmap? = null

    override fun run() {

        // 1. WindowMetricsCalculator 인스턴스 가져오기
        val windowMetricsCalculator = WindowMetricsCalculator.getOrCreate()

        // 2. 현재 창(Activity)의 WindowMetrics 계산
        val metrics = windowMetricsCalculator.computeCurrentWindowMetrics(activity)

        // 3. 높이(height)와 너비(width) 구하기
        screenHeight = metrics.bounds.height()
        screenWidth = metrics.bounds.width()

        //캐릭터 크기 지정
//        characterBitmap = Bitmap.createScaledBitmap(
//            originalBitmap,
//            gameState.player.radius.toInt()*2,
//            gameState.player.radius.toInt()*2,
//            true // 필터링 적용 여부. true를 권장
//        )

        //캐릭터 크기 지정
        aspectRatioCharacter = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        newWidthCharacter = gameState.player.radius
        newHeightCharacter = newWidthCharacter / aspectRatioCharacter // 비율에 맞춰 높이 자동 계산

        characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)




        //침대 위치
        var aspectRatio = bedBitmap.width.toFloat() / bedBitmap.height.toFloat()
        var x = screenWidth - 900f
        var y = 900f
        var newWidth = 500f
        var newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
        val pain = Color.argb(255, 173, 255, 47)
//        var pain = Color.argb(0, 0, 0, 0)
        gameState.walls?.add(GameWall(x, y, x + newWidth, y + newHeight, pain))
        bedRect = RectF(x, y, x + newWidth, y + newHeight)

        //창문
        aspectRatio = smallWindowBitmap.width.toFloat() / smallWindowBitmap.height.toFloat()
        x = screenWidth - 800f
        y = 150f
        newWidth = 300f
        newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
        gameState.walls?.add(GameWall(x, y, x + newWidth, y + newHeight, pain))
        smallWindowRect = RectF(x, y, x + newWidth, y + newHeight)

        //책상
        aspectRatio = deskBitmap.width.toFloat() / deskBitmap.height.toFloat()
        x = screenWidth - 270f
        y = 300f
        newWidth = 250f
        newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
        gameState.walls?.add(GameWall(x, y, x + newWidth, y + newHeight, pain))
        deskRect = RectF(x, y, x + newWidth, y + newHeight)

        //옷장
        aspectRatio = wardrobeBitmap.width.toFloat() / wardrobeBitmap.height.toFloat()
        x = screenWidth - 230f
        y = 520f
        newWidth = 280f
        newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
        gameState.walls?.add(GameWall(x, y, x + newWidth, y + newHeight, pain))
        wardrobeRect = RectF(x, y, x + newWidth, y + newHeight)



//        var pain = Color.argb(255, 173, 255, 47)
//        var pain = Color.argb(0, 0, 0, 0)
//        // 벽 생성
//        gameState.walls?.add(GameWall(10f, 30f, 300f, 900f, pain))
//
//        //창문
//        gameState.wallsNot?.add(GameWall(screenWidth - 400f, 90f, screenWidth-10f, 400f, pain))
//
//        //옷장
//        gameState.walls?.add(GameWall(screenWidth - 200f, 800f, screenWidth-10f, 1500f, pain))
//
//        //wallsNot
//        gameState.wallsNot?.add(GameWall(10f, 950f, 300f, 1200f, Color.argb(0, 0, 0, 0)))
//        gameState.wallsNot?.add(GameWall(screenWidth - 150f, 720f, screenWidth-10f, 980f, Color.argb(0, 0, 0, 0)))

        //캐릭터 위치 지정
        gameState.player.setBounds(screenWidth/2f, screenHeight-500f)

        var startTime: Long
        var timeMillis: Long
        var waitTime: Long


///
//
        //백그라운드 적용
        initializeBackground(screenWidth,screenHeight)

        while (isRunning) {
//            Log.d("canvas", "각도 11: ${joystickState.angle} px")
//            Log.d("canvas", "각도 12: ${joystickState.strength} px")

            startTime = System.currentTimeMillis()
            try {
//                // 1. 입력 및 업데이트 (Update Logic)
                updateGame()

//                // 2. 렌더링 (Draw Logic)px")
//                Log.d("canvas", "각도 2: ${joystickState.strength} px")

                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    if (canvas != null) {
                        drawGame(canvas!!)
                        drawWallItems(canvas!!)
                        drawFurnitureItems(canvas!!)
                        drawItems(canvas!!)
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
//            val currentTime = System.currentTimeMillis()
//            if (currentTime - lastSpawnTime > spawnInterval && gameState.items.size < spawnIntervalUntil && spawnIntervalSwitch) {
//                gameState.spawnItem(screenWidth, screenHeight)
//                lastSpawnTime = currentTime
//            }

//            Log.d("canvas", "각도 1: ${accessItemMake} px")
//            Log.d("canvas", "각도 2: ${accessItemMake} px")

            //한번 돌았을 때 아이템 갯수대로 아이템 만들기
            if(accessItemMake){
                gameState.fixSpawnItems(screenWidth, screenHeight)
                accessItemMake = !accessItemMake
            }

            if(gameState.items.size == spawnIntervalUntil){
                spawnIntervalSwitch = false
            }

            // 3. 아이템 충돌 감지 및 처리
            checkItemCollisions()

            // 벽 충돌 로직
            canvas?.let { nonNullCanvas ->
                checkWallItemCollisions(canvas!!)
            }

            //전체 화면 막기
            val checkScreenIn = isWithinScreenBounds()
            if(!checkScreenIn){
                gameState.player.setBounds(gameState.playerLastX, gameState.playerLastY)
//                canvas?.drawBitmap(characterBitmap!!, gameState.player.x, gameState.player.y, null)


                characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)
                canvas?.drawBitmap(originalBitmap, null, characterRect, null)

//                val circlePaint = Paint().apply {
//                    isAntiAlias = true           // 중요: 테두리를 부드럽게 (계단 현상 제거)
//                    color = Color.BLUE           // 색상 지정 (Color.RED, Color.BLACK 등)
//                    style = Paint.Style.FILL     // 원 내부를 가득 채움
//                }
//                canvas?.drawOval(characterRect, circlePaint)
            }

            try {
                if (waitTime > 0) {
                    sleep(waitTime)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    fun initializeBackground(width: Int, height: Int) {
        // 1. 기존 비트맵이 있다면 해제
        preRenderedBackgroundBitmap?.recycle()

        // 2. 새로운 Bitmap 객체 생성 (화면 크기에 맞게)
        preRenderedBackgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // 3. 이 Bitmap에 그리기 위한 Canvas 생성
        val backgroundCanvas = Canvas(preRenderedBackgroundBitmap!!)

        // 4. 이 'backgroundCanvas'에 모든 정적 배경 요소를 그림 (딱 한 번)
        backgroundCanvas.apply {
            drawColor(Color.rgb(231, 228, 180))
            val image = BitmapFactory.decodeResource(context.resources, R.drawable.room_background2)

            if (image != null) {
                // 1. 원본 비트맵 전체를 지정할 소스 사각형 (srcRect)
                val srcRect = Rect(0, 0, image.width, image.height)

//                // 이미지와 뷰의 비율 계산
//                val imageRatio = image.width.toFloat() / image.height.toFloat()
//                val viewRatio: Float = width.toFloat() / height.toFloat()
//                // 2. 종횡비를 유지하며 뷰 내부에 모두 들어오도록 스케일 팩터 계산
//                // Contain 방식: 두 비율 중 작은 값(min)을 스케일 팩터로 사용
//                val scaleFactor: Float = if (imageRatio > viewRatio) {
//                    width.toFloat() / image.width // 너비에 맞춤
//                } else {
//                    height.toFloat() / image.height // 높이에 맞춤
//                }
//
//                // 3. 이미지가 실제로 그려질 최종 크기와 시작 위치 계산
//                val scaledWidth = image.width * scaleFactor
//                val scaledHeight = image.height * scaleFactor
//
//                // 중앙 정렬을 위한 시작점 (여백 계산)
//                val startX = (width - scaledWidth) / 2
//                val startY = (height - scaledHeight) / 2
//
//
//                val dstRect = Rect(
//                    startX.toInt(),
//                    startY.toInt(),
//                    (startX + scaledWidth).toInt(),
//                    (startY + scaledHeight).toInt()
//                )



                // 2. 이미지가 그려질 화면의 전체 영역을 지정할 대상 사각형 (dstRect)
                // 즉, 0,0 에서 뷰의 너비, 높이까지
                val dstRect = Rect(0, 0, width, height)

                // 3. srcRect의 이미지를 dstRect에 맞게 그립니다.
                // 이미지가 dstRect에 맞춰 늘어나거나 줄어들게 됩니다.
                drawBitmap(image, srcRect, dstRect, null)

                // 리소스 비트맵은 한 번 로드 후 더 이상 필요 없다면 해제 (메모리 절약)
                image.recycle()
            }

//            drawBitmap(image, 0f, 0f, null)
            val linePaint = Paint().apply { color = Color.GRAY; strokeWidth = 1f }
//            drawLine(0f, 100f, width.toFloat(), 100f, linePaint)
            drawLine(0f, height / 4f, width.toFloat(), height / 4f, linePaint)
        }
    }

    /**
     * 조이스틱 입력에 따라 캐릭터 위치를 업데이트합니다.
     */
    private fun updateGame() {

        //이전 캐릭터 좌표 저장
        gameState.playerLastX = gameState.player.x
        gameState.playerLastY = gameState.player.y

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
            gameState.player.x += moveX
            gameState.player.y += moveY

            gameState.player.move(gameState.player.x, gameState.player.y)
        }
    }

    /**
     * Canvas에 캐릭터 비트맵을 그립니다.
     */
    private fun drawGame(canvas: Canvas) {
        // 배경을 지웁니다.
//        canvas.drawColor(Color.rgb(231, 228, 180))

        preRenderedBackgroundBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

//        if (gameView.gameStateDirection == GameState.GameStateFlow.ZOOMING || gameView.gameStateDirection == GameState.GameStateFlow.CLEANING_MODE) {
////            Log.d("currentScale", "ZOOMING: $k++}")
////            Log.d("currentScale", "ZOOMING: $nearestWall}")
//
//            canvas.withSave {
//                translate(gameView.currentPanX, gameView.currentPanY+500)
//                // 목표 아이템의 맵 좌표를 중심으로 확대
//                scale(
//                    gameView.currentScale,
//                    gameView.currentScale,
//                    gameView.targetFocusX,
//                    gameView.targetFocusY
//                )
//
//                drawBitmap(characterBitmap, gameState.player.x, gameState.player.y, null)
//
//                val paint = Paint()
//                paint.color = nearestWall!!.color
//                canvas.drawRect(RectF(nearestWall!!.left, nearestWall!!.top, nearestWall!!.right, nearestWall!!.bottom), paint)
//            }
//        }else{
            gameState.player.setBounds(gameState.player.x, gameState.player.y)

            // 캐릭터를 현재 위치에 그립니다.
            // drawBitmap(비트맵, 그릴 X 좌표, 그릴 Y 좌표, Paint 객체)
//            canvas.drawBitmap(characterBitmap!!, gameState.player.x, gameState.player.y, null)


        characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)

        canvas.drawBitmap(originalBitmap, null, characterRect, null)

//        val circlePaint = Paint().apply {
//            isAntiAlias = true           // 중요: 테두리를 부드럽게 (계단 현상 제거)
//            color = Color.BLUE           // 색상 지정 (Color.RED, Color.BLACK 등)
//            style = Paint.Style.FILL     // 원 내부를 가득 채움
//        }
//        canvas.drawOval(characterRect, circlePaint)

//        }

    }

    /**
     * 플레이어와 모든 아이템 간의 충돌을 확인하고 처리합니다.
     */
    private fun checkItemCollisions() {

        val player = gameState.player // Player 객체에 직접 접근
        gameState.items.removeIf { item ->
            // 1. 충돌 감지 로직 (원형 충돌, 거리 제곱 사용으로 성능 최적화)
            val dx = player.x - item.x
            val dy = player.y - item.y
            val distanceSquared = dx * dx + dy * dy

            // player와 item 클래스에 radius 속성(size / 2)이 추가되었다고 가정
            val combinedRadius = player.radius + item.radius
            val combinedRadiusSquared = combinedRadius * combinedRadius

            val isColliding = distanceSquared <= combinedRadiusSquared

            if (isColliding) {
                Log.d("Joystick", "item get : ${item.x} , ${item.y}")
//                Log.d("Joystick", "combinedRadius: ${combinedRadius}")


                //가까운 벽 체크
//                var minDistanceSq = Float.MAX_VALUE
//                for (wall in gameState.walls!!) {
//                    // 거리의 제곱 계산 (성능 최적화)
//                    val dx = wall.right - item.x
//                    val dy = wall.bottom - item.y
//                    val distanceSq = dx * dx + dy * dy
//
//                    if (distanceSq < minDistanceSq) {
//                        minDistanceSq = distanceSq
//                        nearestWall = wall
//                    }
//                }
//
//                gameView.post {
//                    gameView.startZoomInAnimation(
//                        item.x,
//                        item.y,
//                        screenWidth,
//                        screenHeight
//                    )
//                }

//                gameView.gameStateDirection = GameState.GameStateFlow.ZOOMING


                //페이지 이동 해봐
                gameView.checkGameOverCondition()

            }

            // isColliding이 true이면 해당 아이템을 리스트에서 제거합니다.
            isColliding
        }
    }

    private fun checkWallItemCollisions(canvas: Canvas){
        Log.d(TAG, "checkWallItem!")
        Log.d(TAG, "checkWallItem!" + gameState.walls?.size)


        characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)


        // 벽(장애물)과의 충돌 감지 및 반응
        for (wall in gameState.walls!!) {
            val wallRec = RectF(wall.left, wall.top, wall.right, wall.bottom)
//            val wallRec = RectF(wall.left, wall.top, wall.right, wall.bottom)
//            if (checkCircleRectangleCollision(gameState.player.x, gameState.player.y, gameState.player.radius, wallRec)) {
//
//                Log.d(TAG, "checkWallItem2!")
//                Log.d(TAG, "checkWallItem2!" + gameState.walls?.size)
//
////                gameState.player.setBounds(gameState.playerLastX, gameState.playerLastY)
//////                canvas.drawBitmap(characterBitmap!!, gameState.player.x, gameState.player.y, null)
////
////                characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)
////
////                canvas.drawBitmap(originalBitmap, null, characterRect, null)
//                break
//            }

            if (RectF.intersects(characterRect, wallRec)) {
                // 충돌 발생!
                // 여기서 캐릭터를 멈추거나 튕겨내는 코드를 작성하세요.
                Log.d("Game", "벽에 부딪혔습니다!")

                gameState.player.setBounds(gameState.playerLastX, gameState.playerLastY)
                characterRect = RectF(gameState.player.x, gameState.player.y, gameState.player.x + newWidthCharacter, gameState.player.y + newHeightCharacter)
                canvas.drawBitmap(originalBitmap, null, characterRect, null)

//                val circlePaint = Paint().apply {
//                    isAntiAlias = true           // 중요: 테두리를 부드럽게 (계단 현상 제거)
//                    color = Color.BLUE           // 색상 지정 (Color.RED, Color.BLACK 등)
//                    style = Paint.Style.FILL     // 원 내부를 가득 채움
//                }
//                canvas.drawOval(characterRect, circlePaint)



                break
            }
        }
    }

    //전체 화면 충돌 로직
    fun isWithinScreenBounds(): Boolean {
        // 허용 가능한 최소/최대 X 좌표
        val minX = 0
        val maxX = screenWidth - gameState.player.radius

        // 허용 가능한 최소/최대 Y 좌표
        val minY = 0
        val maxY = screenHeight - gameState.player.radius*2

        // targetX와 targetY가 각 범위 내에 있는지 확인
        val isXValid = gameState.player.x >= minX && gameState.player.x <= maxX
        val isYValid = gameState.player.y >= minY && gameState.player.y <= maxY

        return isXValid && isYValid
    }

//    private fun checkCollision(rect1: RectF, rect2: RectF): Boolean {
//        // RectF.intersect() 메서드를 사용하여 간편하게 충돌을 확인할 수 있습니다.
//        return RectF.intersects(rect1, rect2)
//    }

    fun checkCircleRectangleCollision(
        centerX: Float,
        centerY: Float,
        radius: Float,
        wallRect: RectF
    ): Boolean {

        // Step 1: 직사각형 B에서 원의 중심 C와 가장 가까운 점 P 찾기
        val closestX = Math.max(wallRect.left, Math.min(centerX, wallRect.right))
        val closestY = Math.max(wallRect.top, Math.min(centerY, wallRect.bottom))

        // Step 2: C(centerX, centerY)와 P(closestX, closestY) 사이의 거리 제곱 D^2 계산
        val deltaX = centerX - closestX
        val deltaY = centerY - closestY

        val distanceSquared = (deltaX * deltaX) + (deltaY * deltaY)

        // Step 3: 충돌 여부 판단 (D^2 <= R^2)
        val radiusSquared = radius * radius

        return distanceSquared <= radiusSquared
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
        if (gameView.gameStateDirection == GameState.GameStateFlow.COMMON) {
            for (item in gameState.items) {
                val paint = when (item.type) {
                    ItemType.DEFAULT -> defaultItemPaint
                    ItemType.SPEED_BOOST -> speedBoostItemPaint
                    ItemType.HEALTH_PACK -> healthPackItemPaint
                }

                // 아이템을 원형으로 그립니다.
//                canvas.drawBitmap(windowCleanerToolBitmap, item.x, item.y, null)

            }
        }
    }

    private fun drawWallItems(canvas: Canvas) {
        if (gameView.gameStateDirection == GameState.GameStateFlow.COMMON) {
            for (wall in gameState.walls!!) {

                val paint = Paint()
                paint.color = wall.color

                canvas.drawRect(RectF(wall.left, wall.top, wall.right, wall.bottom), paint)
            }


//            val newWidth = 500f         // 원하는 너비 (예시)
//            val newHeight = 500f        // 원하는 높이 (예시)
//            val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산

//            val paint = Paint().apply {
//                isAntiAlias = true     // 이미지 외곽선의 계단 현상 제거 (안티앨리어싱)
//                isFilterBitmap = true  // 비트맵 확대/축소 시 픽셀을 부드럽게 보정 (Bilinear Filtering)
//                isDither = true        // 색상 표현을 더 부드럽게 (그라데이션 등에서 유리)
//                alpha = 128            // (기존에 쓰시던 반투명 설정 유지)
//            }
//
////            val paint = Paint().apply { alpha = 128 } // 반투명
////            canvas.drawBitmap(bedAreaBitmap, screenWidth-900f, 500f, null)
////            val destRect = RectF(x, y, x + newWidth, y + newHeight)
////            canvas.drawBitmap(bedAreaBitmap, null, destRect, paint)
//
//            val aspectRatio = bedAreaBitmap.width.toFloat() / bedAreaBitmap.height.toFloat()
//            val newWidth = 500f
//            val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산

//            val destRect = RectF(x, y, x + newWidth, y + newHeight)
//            canvas.drawBitmap(bedAreaBitmap, null, destRect, null)

        }
    }

    private fun drawFurnitureItems(canvas: Canvas) {
        makeBed()

        makeSmallWindow()

        makeDesk()

        makeWardrobe()
    }

    fun makeBed(){
//        val aspectRatio = bedBitmap.width.toFloat() / bedBitmap.height.toFloat()
//        val x = screenWidth - 950f
//        val y = 700f
//        val newWidth = 750f
//        val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
////            canvas.drawBitmap(smallWindowAreaBitmap, x_window, y_window, null)
//
//        val destRect = RectF(x, y, x + newWidth, y + newHeight)
        canvas?.drawBitmap(bedBitmap, null, bedRect, null)
    }

    fun makeSmallWindow(){
//        val aspectRatio = smallWindowBitmap.width.toFloat() / smallWindowBitmap.height.toFloat()
//        val x = screenWidth - 400f
//        val y = 100f
//        val newWidth = 350f
//        val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
////            canvas.drawBitmap(smallWindowAreaBitmap, x_window, y_window, null)
//
//        val destRect = RectF(x, y, x + newWidth, y + newHeight)
        canvas?.drawBitmap(smallWindowBitmap, null, smallWindowRect, null)
    }

    fun makeDesk(){
//        val aspectRatio = deskBitmap.width.toFloat() / deskBitmap.height.toFloat()
//        val x = screenWidth - 330f
//        val y = 270f
//        val newWidth = 350f
//        val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
////            canvas.drawBitmap(smallWindowAreaBitmap, x_window, y_window, null)
//
//        val destRect = RectF(x, y, x + newWidth, y + newHeight)
        canvas?.drawBitmap(deskBitmap, null, deskRect, null)
    }

    fun makeWardrobe(){
//        val aspectRatio = wardrobeBitmap.width.toFloat() / wardrobeBitmap.height.toFloat()
//        val x = screenWidth - 400f
//        val y = 400f
//        val newWidth = 600f
//        val newHeight = newWidth / aspectRatio // 비율에 맞춰 높이 자동 계산
////            canvas.drawBitmap(smallWindowAreaBitmap, x_window, y_window, null)
//
//        val destRect = RectF(x, y, x + newWidth, y + newHeight)
        canvas?.drawBitmap(wardrobeBitmap, null, wardrobeRect, null)
    }

//    fun startZoomInAnimation(objectCenterX: Float, objectCenterY: Float) {
//        targetFocusX = objectCenterX
//        targetFocusY = objectCenterY
//
//        // 뷰 중앙에 목표 아이템이 오도록 최종 Pan 목표 계산
//        val targetPanX = (screenWidth / 2f) - (targetFocusX * zoomTargetScale)
//        val targetPanY = (screenHeight / 2f) - (targetFocusY * zoomTargetScale)
//
//        // ValueAnimator를 사용하여 currentScale, currentPanX/Y 값을 부드럽게 변경
//        animator = ValueAnimator.ofFloat(0f, 1f).apply {
//            duration = 500L
//
//            addUpdateListener {
//                val fraction = it.animatedValue as Float
//
//                currentScale = 1.0f + (zoomTargetScale - 1.0f) * fraction
//                currentPanX = targetPanX * fraction
//                currentPanY = targetPanY * fraction
//
//                // SurfaceView는 invalidate() 대신 렌더링 스레드가 다음 프레임을 그리도록 합니다.
//                // (대부분의 SurfaceView 게임 루프는 자동으로 화면을 계속 갱신합니다.)
//            }
//
////            addListener(object : AnimatorListenerAdapter() {
////                override fun onAnimationEnd(animation: Animator) {
////                    // 애니메이션 종료 후 청소 모드로 전환
////                    gameStateDirection = GameState.GameStateFlow.CLEANING_MODE
////                    // startCleaningMode() 호출
////                }
////            })
//            start()
//        }
//    }
}