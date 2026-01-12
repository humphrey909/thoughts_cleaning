package com.example.thoughts_cleaning.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.SlideGameBall
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListDto
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

class GameSlideView(context: Context, attrs: AttributeSet? = null) : View(context, attrs), GestureDetector.OnGestureListener {
    // 1. 그래픽 도구
//    private val goalPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.FILL; isAntiAlias = true }
//    private val goalTextPaint = Paint().apply { color = Color.WHITE; textSize = 60f; textAlign = Paint.Align.CENTER }
    private val ballPaint = Paint().apply { isAntiAlias = true }

    // 골대 입구 테두리 선 (입체감 용)
//    private val goalBorderPaint = Paint().apply { color = Color.rgb(50, 50, 50); style = Paint.Style.STROKE; strokeWidth = 8f; isAntiAlias = true }

    // 2. 게임 오브젝트
    private val balls = mutableListOf<SlideGameBall>() // 공 5개를 담을 리스트
    private var ballRadius = 80f

    // 스트라이커 변수
    private var strikerX = 0f
    private var strikerY = 0f
    private val strikerRadius = 60f // 스트라이커는 좀 더 큼
    private var isHoldingStriker = false

    // 상단 골인 지점 (넓은 공간)
    private val goalRect = RectF()
    private val goalHeightRatio = 0.1f


    private var holeX = 0f
    private var holeY = 0f
    private val holeRadius = 80f

    // 3. 물리 변수
    private val friction = 0.98f // 마찰력
    private val goalFriction = 0.85f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var strikerVx = 0f // 스트라이커의 순간 속도
    private var strikerVy = 0f

    // 4. 제스처 감지기
    private val gestureDetector = GestureDetector(context, this)
    private var selectedBallIndex = -1 // 현재 터치 중인 공의 인덱스 (-1이면 없음)

    private var isDragging = false

    var isAnyBallMoving = false // 움직이는 공이 하나라도 있는지 체크

    private val ballSmallBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.img_small_dust3)

    private val ballBigBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.img_big_dust)

    private var strikerOriginalBitmap: Bitmap? = null
    private var strikerScaledBitmap: Bitmap? = null


//    private val ballMediumBitmap: Bitmap =
//        BitmapFactory.decodeResource(context.resources, R.drawable.img_dust_medium)
//    private val ballLargeBitmap: Bitmap =
//        BitmapFactory.decodeResource(context.resources, R.drawable.img_dust_large)

    private lateinit var ballRect:RectF

    private var entranceCleanBitmap: Bitmap? = null

    private val ballTextPaint = Paint().apply {
        color = Color.BLACK // 글자색 (흰색)
        textSize = 45f      // 글자 크기
        textAlign = Paint.Align.CENTER // 좌우 중앙 정렬
        typeface = Typeface.DEFAULT_BOLD // 굵은 글씨
        isAntiAlias = true
    }


    // [추가] 하단 버튼 영역만큼의 여백 (픽셀 단위)
    // 버튼 높이에 맞춰서 이 숫자를 조절하세요 (예: 200f ~ 300f)
    private val bottomPadding = 50f

    // 실제 공이 튕기는 바닥 라인 (계산됨)
    private var playableBottom = 0f


    // [신규] 게임 규칙 변수
    var maxChances = 5 // 총 기회
    var currentChances = maxChances // 남은 기회

    var isStrikerReturning = false // 스트라이커가 돌아가는 중인가?
    var isStrikerLocked = false    // 스트라이커 조작 잠금 (복귀 중일 때)

    // 이월된 벌칙 공 개수 (Activity에서 넣어줘야 함)
    var penaltyCount = 0

    // 스트라이커 초기 위치 저장용
    private var strikerOriginX = 0f
    private var strikerOriginY = 0f

    // [추가] 치고 나서 대기 중인지 확인하는 변수
    private var isWaitingForReturn = false

//    var contentList: ResThoughtOfUserListCustomDto? = null
    var contentList: ResThoughtOfUserListDto? = null

    private var targetLabels: List<String> = emptyList()
//    private val targetLabels = listOf("A")
    private var targetLabel = ""

    // 스트라이커 (내가 조종하는 하얀 공) 페인트
//    private val strikerPaint = Paint().apply { color = Color.WHITE; style = Paint.Style.FILL; isAntiAlias = true; shadowLayer(10f, 0f, 0f, Color.GRAY) }
    private val strikerBorderPaint = Paint().apply { color = Color.BLACK; style = Paint.Style.STROKE; strokeWidth = 5f; isAntiAlias = true }

    private val strikerPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true

        // shadowLayer -> setShadowLayer로 변경해야 합니다.
        setShadowLayer(20f, 0f, 0f, Color.GRAY)
    }

    var aspectRatioCharacter = 0f
    var newWidthCharacter = 0f
    var newHeightCharacter = 0f

    private var backgroundBitmap: Bitmap? = null
    private val backgroundRect = RectF() // 화면 크기를 저장할 사각형

    // [추가] 스트라이커 속도 계산용 변수
    private var prevStrikerX = 0f
    private var prevStrikerY = 0f
    private var currentStrikerSpeed = 0f // 현재 휘두르는 속도


    private var hasHitBigBall = false // 이번 터치에서 왕먼지를 건드렸는가?

    init {
        // 그림자 효과 및 부드러운 그라데이션을 위해 소프트웨어 렌더링 사용
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        try {
            strikerOriginalBitmap = BitmapFactory.decodeResource(resources, R.drawable.dust_rolling_tool2)
        } catch (e: Exception) {
            // 이미지가 없을 경우를 대비한 예외 처리
            e.printStackTrace()
        }
    }

    interface OnGameEndListener {
        fun onGameOver(successText: String)
        fun onGameFail(remainCount: Int)
    }
    var gameEndListener: OnGameEndListener? = null

    fun setBallData(content: String) {
        this.targetLabel = content

        if (width > 0 && height > 0) {
            initBalls(width, height)
            invalidate()
        }
    }

    fun setBallList(contentList: ResThoughtOfUserListDto) {
        if (contentList == null) return

//        targetLabels = contentList.thoughtsList.map { it.contentThought }

        this.contentList = contentList

        if (width > 0 && height > 0) {
            initBalls(width, height)
            invalidate()
        }
    }

    private fun initBalls(w: Int, h: Int) {
        balls.clear()

        strikerOriginX = w / 2f
        strikerOriginY = playableBottom - (strikerRadius + 100f) // 바닥 위

        var totalCount = this.contentList!!.thoughtsList.size-1 //현재 생각은 제거

        // 저장된 targetLabels를 사용해서 공 생성
        val count = totalCount
//        if (count == 0) return

//        val startY = h * 0.8f
//        val spacing = w.toFloat() / (count + 1)

//        val colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN)

//        Log.d("PreferenceManager", "balls list")
//        Log.d("PreferenceManager", "balls list : ${this.contentList}")
//        Log.d("PreferenceManager", "balls list : ${this.contentList!!.thoughtsListCustom}")


        val strikerX = w / 2f
        val strikerY = h * 0.85f

        val bigRadius = ballRadius * 2.5f
        val bigDustY = strikerY - (strikerRadius + bigRadius + 50f)

        //현재의 먼지 그리기
        balls.add(SlideGameBall(
            x = strikerX, y = bigDustY,
            color = Color.DKGRAY,
            sizeType = "BIG",
            text = this.contentList!!.thoughtsList.get(totalCount).contentThought, // 여기서 저장된 글자를 사용
            radius = bigRadius,
            startX = strikerX,
            startY = bigDustY,
            friction = 0.995f,
            mass = 50.0f,
        ))



        if (count == 0) return

        val random = java.util.Random()

        // [핵심] 작은 공이 나올 수 있는 Y축 범위 계산
        // Top(최소): 골대(입구)보다 아래 + 여유 공간
        val spawnMinY = goalRect.bottom + ballRadius * 3

        // Bottom(최대): 큰 공보다 위 - 여유 공간
        val spawnMaxY = (bigDustY - bigRadius) - ballRadius * 2

        // 만약 공간이 너무 좁으면 그냥 화면 전체로 예외 처리
//        val safeMaxY = if (spawnMaxY > spawnMinY) spawnMaxY else (h - ballRadius * 4)
        val safeMaxY = if (spawnMaxY > spawnMinY) spawnMaxY else (playableBottom - ballRadius * 4)

        var ballIdx = 0
        for (i in 0 until count) {

            var randomX: Float = 0f
            var randomY: Float = 0f
            var isSafePosition = false
            var attempts = 0 // 무한루프 방지용

            // [핵심] 안전한 위치를 찾을 때까지 랜덤 돌리기 (최대 100번 시도)
            while (!isSafePosition && attempts < 100) {
                attempts++

                // 1. 화면 전체 범위 내에서 랜덤 좌표 생성 (벽에 너무 붙지 않게 radius만큼 여유 둠)
                // 범위: ballRadius ~ (width - ballRadius)
//                randomX = random.nextFloat() * (w - 2 * ballRadius) + ballRadius
//                randomY = random.nextFloat() * (h - 2 * ballRadius) + ballRadius


                // X축: 좌우 벽 안쪽 랜덤
                randomX = random.nextFloat() * (w - 2 * ballRadius) + ballRadius

                // Y축: 위에서 계산한 범위(골대 아래 ~ 큰공 위) 내에서 랜덤
                randomY = spawnMinY + random.nextFloat() * (safeMaxY - spawnMinY)

                // 2. [검사] 골대 영역과 겹치는지 확인 (시작하자마자 골인 방지)
                // 골대보다 조금 더 넓게 여유 공간을 둡니다.
                if (randomY < goalRect.bottom + ballRadius * 2) {
                    continue // 너무 위쪽이면 다시 뽑기
                }

                // 3. [검사] 스트라이커(흰 공) 위치와 겹치는지 확인 (시작하자마자 충돌 방지)
                // 스트라이커 예상 위치: (w/2, h*0.85)

                val distToStriker = hypot(randomX - strikerX, randomY - strikerY)

                // 스트라이커와 겹치면 다시 뽑기
                if (distToStriker < ballRadius + strikerRadius + 50f) {
                    continue
                }

                // 4. [검사] 기존에 생성된 다른 공들과 겹치는지 확인 (선택사항)
                var isOverlappingBall = false
                for (existingBall in balls) {
                    val dist = hypot(randomX - existingBall.x, randomY - existingBall.y)
//                    if (dist < ballRadius * 2 + 10f) { // 두 공 지름보다 가까우면 겹침
//                        isOverlappingBall = true
//                        break
//                    }

                    if (dist < ballRadius + existingBall.radius + 5f) {
                        isOverlappingBall = true
                        break
                    }
                }
                if (isOverlappingBall) continue

                // 모든 검사 통과!
                isSafePosition = true
            }




            balls.add(SlideGameBall(
                x = randomX, y = randomY,
                color = Color.RED,
                sizeType = "SMALL",
                text = this.contentList!!.thoughtsList.get(i).contentThought, // 여기서 저장된 글자를 사용
                radius = ballRadius,
                startX = randomX,
                startY = randomY,
                friction = 0.95f,
                mass = 1.0f
            ))
            ballIdx++






//            val bx = spacing * (idx + 1)
//            val by = startY


//            for (idx in 0 .. count) {
//                val bx = spacing * (ballIdx + 1)
//                val by = startY
//
//                balls.add(SlideGameBall(
//                    x = bx, y = by,
//                    color = Color.RED,
//                    sizeType = "",
//                    text = "", // 여기서 저장된 글자를 사용
//                    startX = bx,
//                    startY = by,
//                    friction = 0.98f
//                ))
//                ballIdx++
//            }


//            if(data.sizeType == "SMALL"){
//                for (idx in 0 .. data.count - 1) {
//                    val bx = spacing * (ballIdx + 1)
//                    val by = startY
//
//                    balls.add(SlideGameBall(
//                        x = bx, y = by,
//                        color = Color.RED,
//                        sizeType = data.sizeType,
//                        text = "", // 여기서 저장된 글자를 사용
//                        startX = bx,
//                        startY = by,
//                        friction = 0.98f
//                    ))
//                    ballIdx++
//                }
//            }else if(data.sizeType == "MEDIUM"){
//
////                Log.d("PreferenceManager", "balls count")
////                Log.d("PreferenceManager", "balls count : ${data.count}")
//
//                for (idx in 0 .. data.count - 1) {
//                    val bx = spacing * (ballIdx + 1)
//                    val by = startY
//
//                    balls.add(SlideGameBall(
//                        x = bx, y = by,
//                        color = Color.RED,
//                        sizeType = data.sizeType,
//                        text = "", // 여기서 저장된 글자를 사용
//                        startX = bx,
//                        startY = by,
//                        friction = 0.92f
//                    ))
//                    ballIdx++
//                }
//            }else if(data.sizeType == "LARGE"){
//                for (idx in 0 .. data.count - 1) {
//                    val bx = spacing * (ballIdx + 1)
//                    val by = startY
//
//                    balls.add(SlideGameBall(
//                        x = bx, y = by,
//                        color = Color.RED,
//                        sizeType = data.sizeType,
//                        text = "", // 여기서 저장된 글자를 사용
//                        startX = bx,
//                        startY = by,
//                        friction = 0.88f
//                    ))
//                    ballIdx++
//                }
//            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 상단 골인 영역 설정 (전체 너비, 상단 20%)

        // [핵심 수정] 1. 실제 게임 바닥 라인 설정 (전체 높이에서 버튼 공간만큼 뺌)
        playableBottom = h - bottomPadding

        // [핵심 수정] 2. 스트라이커 위치를 바닥 라인 위로 올림
        strikerX = w / 2f
        // 기존: h * 0.85f -> 수정: 바닥 라인에서 조금 위(스트라이커 반지름 + 여유)
        strikerY = playableBottom - (strikerRadius + 100f)

        prevStrikerX = strikerX
        prevStrikerY = strikerY

        strikerOriginalBitmap?.let { bitmap ->
            // 지름 계산 (반지름 * 2)
            val diameter = (strikerRadius * 2).toInt()

            // 안전장치: 크기가 유효할 때만 생성
            if (diameter > 0) {
                strikerScaledBitmap = Bitmap.createScaledBitmap(bitmap, diameter, diameter, true)
            }
        }

        if (entranceCleanBitmap == null) {
            // R.drawable.img_goal_zone 부분에 본인의 이미지 파일명을 넣으세요.
            entranceCleanBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.entrance_clean)
        }

        // 1. 화면 크기에 맞춰서 사각형 영역 설정 (이미지 크기 조절 X)
        backgroundRect.set(0f, 0f, w.toFloat(), h.toFloat())

        // 2. 이미지는 딱 한 번만 로딩 (null일 때만)
        if (backgroundBitmap == null) {
            // [중요] 옵션을 사용해 메모리를 절약하며 로딩
            val options = BitmapFactory.Options().apply {
                inScaled = false // 불필요한 자동 스케일링 방지
            }
            backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_background, options)
        }


        // 목표지점 너비: 화면 너비의 절반 (50%)
        val goalWidth = w * 0.5f
        // 왼쪽 시작점: (전체너비 - 목표너비) / 2 = 중앙 정렬
        val goalLeft = (w - goalWidth) / 2f
        val goalRight = goalLeft + goalWidth

        val topMarginRatio = 0.1f // 화면 높이의 10%만큼 아래로 내림 (이 숫자를 조절하세요)
        val goalHeight = h * 0.05f // 골대 자체의 높이

        val goalTop = h * topMarginRatio/2 // 시작점 (0f가 아니라 여백만큼 아래)
        val goalBottom = goalTop + goalHeight // 끝점

        // 3. RectF 설정 (top 자리에 0f 대신 goalTop 사용)
        goalRect.set(goalLeft, goalTop, goalRight, goalBottom)

        if (targetLabels.isNotEmpty()) {
            initBalls(w, h)
        }

        // 스트라이커 초기 위치 (화면 하단 중앙)
//        strikerX = w / 2f
//        strikerY = h * 0.85f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 배경 그리기 (맨 윗줄에 위치해야 함)
        backgroundBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, backgroundRect, null)
        }

        // 1. 상단 골인 영역 그리기
        entranceCleanBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, goalRect, null)
        }

        // 2. 공 그리기
        for (ball in balls) {
            if (ball.isGoal) continue

            ballPaint.color = ball.color

            val currentBitmap = if (ball.sizeType == "BIG") {
                ballBigBitmap // 왕먼지 이미지 (없으면 null)
            } else {
                ballSmallBitmap // 일반 먼지 이미지
            }

            currentBitmap?.let { bitmap ->
//                var ballCommonBitmap: Bitmap? = null
//                ballCommonBitmap = ballSmallBitmap

                aspectRatioCharacter = bitmap.width.toFloat() / bitmap.height.toFloat()
//                aspectRatioCharacter = ballCommonBitmap!!.width.toFloat() / ballCommonBitmap!!.height.toFloat()

//                val diameter = ballRadius * 2
                val diameter = ball.radius
                newWidthCharacter = diameter
                newHeightCharacter = newWidthCharacter / aspectRatioCharacter

                // 2. 위치 보정: (중심 좌표) - (길이의 절반)
                val left = ball.x - (newWidthCharacter / 2)
                val top = ball.y - (newHeightCharacter / 2)
                val right = left + newWidthCharacter
                val bottom = top + newHeightCharacter

                ballRect = RectF(left, top, right, bottom)
                canvas.drawBitmap(bitmap, null, ballRect, null)
            }

            if(ball.sizeType == "BIG"){
                val textY = ball.y - ((ballTextPaint.descent() + ballTextPaint.ascent()) / 2)
                if (ball.text.isNotEmpty()) {
                    val displayText = ball.text.take(5)
                    canvas.drawText(displayText, ball.x, textY, ballTextPaint)
                }
            }


            // 글자의 높이 중심을 구해서 공의 중심(y)에 맞춤
//            val textY = ball.y - ((ballTextPaint.descent() + ballTextPaint.ascent()) / 2)
//            canvas.drawText(ball.text, ball.x, textY, ballTextPaint)

//            if (ball.isReturning || ball.isGoal || abs(ball.vx) > 0.01f || abs(ball.vy) > 0.01f) {
//                isAnyBallMoving = true
//            }

            // 움직임 감지 (골인된 공은 이미 continue 되었으므로 체크 안 함)
            if (ball.isReturning || abs(ball.vx) > 0.01f || abs(ball.vy) > 0.01f) {
                isAnyBallMoving = true
            }
        }

        // 4. 내 스트라이커(채) 그리기
//        canvas.drawCircle(strikerX, strikerY, strikerRadius, strikerPaint)
//        canvas.drawCircle(strikerX, strikerY, strikerRadius, strikerBorderPaint)

        strikerScaledBitmap?.let { bitmap ->
            // 비트맵을 그릴 때는 '좌상단(Left, Top)' 좌표가 필요합니다.
            // 중심 좌표(strikerX, Y)에서 반지름만큼 빼서 계산합니다.
            val left = strikerX - strikerRadius
            val top = strikerY - strikerRadius

            canvas.drawBitmap(bitmap, left, top, null)
        } ?: run {
            // (선택사항) 이미지가 로드되지 않았을 때를 대비한 예비용 원 그리기
            // strikerScaledBitmap이 null일 때만 실행됨
            canvas.drawCircle(strikerX, strikerY, strikerRadius, strikerPaint)
        }


        // 3. 움직이는 공이 하나라도 있으면 물리 업데이트 & 다시 그리기
        if (isAnyBallMoving) {
            updatePhysics()
            invalidate()
        }
    }

    private fun updatePhysics() {
        //공끼리 부딪히는지 먼저 검사
        checkBallToBallCollisions()

        for (ball in balls) {

            if (ball.isGoal) continue

            // ----------------------------------------------------------------
            // [복귀 모드] 큰 공이 여기서 걸려서 돌아가게 됨
            // ----------------------------------------------------------------
            if (ball.isReturning) {
                val dx = ball.startX - ball.x
                val dy = ball.startY - ball.y
                val dist = hypot(dx, dy)

                // 큰 공은 조금 천천히 돌아가게 하고 싶다면?
                // val returnSpeed = if(ball.sizeType == "BIG") 15f else 25f
                val returnSpeed = 20f

                if (dist <= returnSpeed) {
                    // 도착 완료
                    ball.x = ball.startX
                    ball.y = ball.startY
                    ball.vx = 0f
                    ball.vy = 0f
                    ball.isReturning = false
                    // 큰 공은 스트라이커와 겹칠 일이 거의 없으므로 바로 해제해도 됨
                } else {
                    // 이동 중
                    val angle = atan2(dy, dx)
                    ball.x += cos(angle) * returnSpeed
                    ball.y += sin(angle) * returnSpeed
                    ball.vx = 0f; ball.vy = 0f
                }
                continue // 복귀 중엔 다른 물리 연산 건너뜀
            }

            // =================================================================
            // [추가] 진공 청소기 효과 (Suction Effect)
            // =================================================================

            // 1. 빨아들이는 범위 설정 (골대 입구 아래 300px 정도)
            val suctionRange = 300f
            val goalBottom = goalRect.bottom.toFloat()

            // 공이 골대 근처(Y축) & 골대 폭 안쪽(X축)에 들어왔는지 확인
            if (ball.y < goalBottom + suctionRange &&
                ball.y > -ball.radius && // 화면 밖으로 완전히 나가기 전까지만
                ball.x > goalRect.left - 50f && ball.x < goalRect.right + 50f) {

                // 2. 목표지점 설정 (골대 안쪽 깊숙한 곳 중앙)
                val targetX = goalRect.centerX().toFloat()
                val targetY = -100f // 화면 위쪽(천장)보다 더 안쪽

                // 3. 거리와 방향 계산
                val dx = targetX - ball.x
                val dy = targetY - ball.y

                // 4. 빨아들이는 힘 (Suction Strength)
                // 0.05f ~ 0.15f 사이로 조절 (클수록 강력한 모터)
                val suctionPower = 0.12f

                // 현재 속도에 '빨려가는 힘'을 더함 (가속도)
                ball.vx += dx * suctionPower
                ball.vy += dy * suctionPower

                // 5. 저항 감소 (빨려 들어갈 땐 마찰 없이 쑥 들어가게)
                ball.vx *= 0.98f
                ball.vy *= 0.98f
            }
            // ----------------------------------------------------------------
            // 3. [일반 물리 연산] (이동 및 벽 충돌)
            // ----------------------------------------------------------------

            // 속도 적용
            ball.x += ball.vx
            ball.y += ball.vy

            // 마찰력
            ball.vx *= ball.friction
            ball.vy *= ball.friction

            // 벽 튕기기 (좌우)
            if (ball.x - ballRadius < 0) {
                ball.x = ballRadius
                ball.vx = -ball.vx
            } else if (ball.x + ballRadius > width) {
                ball.x = width - ballRadius
                ball.vx = -ball.vx
            }

            // 벽 튕기기 (하단만, 상단은 골인 지점이라 뚫림)
//            if (ball.y + ballRadius > height) {
//                ball.y = height - ballRadius
//                ball.vy = -ball.vy
//            }


            // [수정] 하단 벽 튕기기
            // 기존: if (ball.y + ball.radius > height)
            // 수정: playableBottom 사용
//            if (ball.y + ball.radius > playableBottom) {
//                ball.y = playableBottom - ball.radius // 바닥 라인 위로 강제 이동
//                ball.vy = -ball.vy * ball.elasticity // 튕겨내기
//            }

            // [수정] 바닥 충돌 감지
            // 화면 맨 끝(height)이 아니라, 버튼 위(playableBottom)까지만 내려오게 함
            if (ball.y + ball.radius > playableBottom) {

                ball.y = playableBottom - ball.radius // 바닥 라인 위로 강제 이동

                // 튕겨내기 (반대 방향 속도 + 탄성)
                ball.vy = -ball.vy * ball.elasticity

                // (선택) 바닥 마찰력 적용 (바닥에서 굴러갈 때 속도 줄이기)
                ball.vx *= ball.friction
            }

            // ----------------------------------------------------------------
            // [골인 감지 로직]
            // ----------------------------------------------------------------
            if (ball.y - ball.radius < goalRect.bottom) {

//                if (ball.x > goalRect.left && ball.x < goalRect.right &&
//                    ball.y > goalRect.top) {
                if (ball.x > goalRect.left && ball.x < goalRect.right) {

                    // 골대 깊숙이(화면 끝까지) 들어왔을 때 골인 처리
                    if (ball.y - ball.radius < 0) {
                    // 위로 올라가는 중이라면 골인 판정
                        if (!ball.isGoal && ball.vy < 0) {

                            // [수정 A] 큰 공(왕먼지) 처리
                            if (ball.sizeType == "BIG") {

                                // 1. 기회 1회 차감 (생명력 감소)
                                currentChances--

                                Toast.makeText(context, "남은 기회: $currentChances", Toast.LENGTH_SHORT).show()

                                // 1. 일단 골인 처리하여 화면에서 즉시 사라지게 함
                                ball.isGoal = true
                                ball.vx = 0f
                                ball.vy = 0f

                                // 2. 2초 뒤에 원래 자리에서 부활 (새로 생성된 효과)
//                                postDelayed({
//                                    // 위치를 태초의 위치(startX, startY)로 강제 이동 (텔레포트)
//                                    ball.x = ball.startX
//                                    ball.y = ball.startY
//                                    ball.vx = 0f
//                                    ball.vy = 0f
//
//                                    // 다시 살아남 (골인 상태 해제)
//                                    ball.isGoal = false
//                                    ball.isReturning = false // 혹시 켜져 있을 복귀 모드도 끔
//                                }, 2000) // 2초 대기

                                // 기회가 남았을 때만 부활 예약
                                if (currentChances > 0) {
                                    postDelayed({
                                        ball.x = ball.startX
                                        ball.y = ball.startY
                                        ball.vx = 0f; ball.vy = 0f
                                        ball.isGoal = false
                                        ball.isReturning = false
                                    }, 2000)
                                } else {
                                    // 기회가 0이면 게임 오버 (실패) 처리
                                    checkGameOverOrFail()
                                }
                            }

                            // [수정 B] 작은 공(고민) 처리 (기존 유지)
                            else {
                                ball.isGoal = true
                                ball.vx = 0f
                                ball.vy = 0f
                            }

                            // [수정 C] 게임 종료 체크 (기존 유지)
                            // 큰 공 상태와 상관없이 작은 공들만 다 사라졌는지 확인
                            val smallBalls = balls.filter { it.sizeType != "BIG" }
                            val isAllCleared = smallBalls.all { it.isGoal }

                            if (isAllCleared && smallBalls.isNotEmpty()) {
                                gameEndListener?.onGameOver("모든 고민이 사라졌습니다!")
                            }
                        }
                    }
                }
                // B. [벽 충돌] X축이 골대 밖인가? (왼쪽 or 오른쪽 벽)
                else {
                    // 여기가 핵심입니다!
                    // 입구가 아닌 옆 공간(천장)을 뚫으려고 하면 튕겨냅니다.

                    // 1. 천장(0)에 닿았거나 뚫고 나갔다면?
                    if (ball.y - ball.radius < 0) {
                        ball.y = ball.radius  // 위치 보정 (안으로 밀어넣기)
                        ball.vy = -ball.vy * ball.elasticity // 튕겨내기
                    }

                    // 2. (선택사항) 골대 옆면(벽의 아래쪽 모서리)에 부딪힌 경우 처리
                    // 공이 올라오다가 골대 옆 벽의 '바닥면'에 맞음
                    else if (ball.y - ball.radius < goalRect.bottom && ball.vy < 0) {
                        ball.y = goalRect.bottom + ball.radius
                        ball.vy = -ball.vy * ball.elasticity
                    }
                }
            }

            // ----------------------------------------------------------------
            // 3. [복귀 트리거] 상단 충돌 및 실패 감지
            // ----------------------------------------------------------------

            // 공이 화면 높이의 50% 위로 올라갔을 때 (ball.y < height * 0.5)
//            if (ball.y < height * 0.5f) {
//
//                // CASE A: 상단 벽(천장)에 닿았을 때
//                if (ball.y - ballRadius < 0) {
//                    // 골인 구간인지 확인
//                    if (ball.x > goalRect.left && ball.x < goalRect.right) {
//                        // 골인 성공! (기존 로직)
//                        if (!ball.isGoal && ball.vy < 0) {
//                            ball.isGoal = true
//                        }
//                        if (ball.y - ballRadius < 0) { ball.y = ballRadius; ball.vy = 0f }
//                    }
//                    else {
//                        // [수정] 골인이 아님(빗나감) -> 복귀 모드 발동!
//                        ball.isReturning = true
//                    }
//                }
//
//                // CASE B: 허공에서 멈췄을 때 (속도가 거의 0이 됨)
//                // 골인이 아닌데 위쪽에서 멈춰버리면 게임 진행이 안되므로 복귀시킴
//                if (!ball.isGoal && abs(ball.vx) < 1f && abs(ball.vy) < 1f) {
//                    ball.isReturning = true
//                }
//            }

            // [추가] 스트라이커 제자리로 복귀
            if (isStrikerReturning) {
                // 목표점(Origin)까지 이동
                val dx = strikerOriginX - strikerX
                val dy = strikerOriginY - strikerY
                val dist = hypot(dx, dy)

                val returnSpeed = 30f // 복귀 속도

                if (dist <= returnSpeed) {
                    // 도착 완료
                    strikerX = strikerOriginX
                    strikerY = strikerOriginY
                    isStrikerReturning = false

                    // [중요] 기회가 남았으면 다시 조작 허용, 없으면 게임 오버 체크
                    if (currentChances > 0) {
                        isStrikerLocked = false
                    } else {
                        // 기회를 다 썼는데 공이 남았는지 확인
//                        checkGameOverOrFail()
                    }
                } else {
                    // 이동 중
                    val angle = atan2(dy, dx)
                    strikerX += cos(angle) * returnSpeed
                    strikerY += sin(angle) * returnSpeed
                }
            }
        }
    }

    // 기회 소진 시 결과 판정
    private fun checkGameOverOrFail() {
        // 움직이는 공이 멈출 때까지 조금 기다려야 할 수도 있지만,
        // 여기서는 즉시 판정한다고 가정 (혹은 딜레이를 줄 수도 있음)

        // 남은 공 개수 세기 (왕먼지는 제외하고 작은 공만 셀지, 포함할지 결정)
        val remainingBalls = balls.count { !it.isGoal }

        if (remainingBalls == 0) {
            // 성공! (기존 리스너 호출 등)
        } else {
            // 실패! -> 남은 개수 저장하고 팝업 띄우기
            gameEndListener?.onGameFail(remainingBalls)
        }
    }

    // --- 터치 이벤트 처리 ---
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        // [추가] 복귀 중이거나 기회가 다 끝났으면 터치 무시
        if (isStrikerLocked || currentChances <= 0) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
//                // 터치 시작 시점에는 속도 0으로 시작
//                prevStrikerX = event.x
//                prevStrikerY = event.y
//                currentStrikerSpeed = 0f
//
//                // 스트라이커를 터치했는지 확인
//                val dist = hypot(event.x - strikerX, event.y - strikerY)
//                if (dist <= strikerRadius * 1.5) {
//                    isHoldingStriker = true
//                    lastTouchX = event.x
//                    lastTouchY = event.y
//                    return true
//                }
                // [초기화] 터치 시작할 때마다 변수 초기화
                hasHitBigBall = false
                strikerVx = 0f
                strikerVy = 0f

                lastTouchX = event.x
                lastTouchY = event.y

                // 스트라이커 터치 판정
                val dist = hypot(event.x - strikerX, event.y - strikerY)
                if (dist <= strikerRadius * 1.5) {
                    isHoldingStriker = true
                    return true
                }

            }
            MotionEvent.ACTION_MOVE -> {
//                if (isHoldingStriker) {
//                    // [핵심 추가] 이동 거리(속도) 계산
//                    // (현재 터치 위치 - 직전 터치 위치)
//                    val dx = event.x - prevStrikerX
//                    val dy = event.y - prevStrikerY
//
//                    // 거리가 곧 속도
//                    val speed = hypot(dx, dy)
//
//                    // 노이즈 제거: 너무 미세한 떨림은 무시하거나, 부드럽게 보정(Smoothing)
//                    currentStrikerSpeed = speed
//
//                    // 1. 스트라이커 이동
//                    strikerX = event.x
//                    strikerY = event.y
//
//                    // 2. 내 손가락 속도 계산 (타격감 핵심)
//                    strikerVx = event.x - lastTouchX
//                    strikerVy = event.y - lastTouchY
//
//                    // 3. 충돌 체크 (공 때리기)
//                    checkCollisionWithBalls()
//
//                    lastTouchX = event.x
//                    lastTouchY = event.y
//                    invalidate()
//                }

                if (isHoldingStriker) {

                    // 1. 내 손가락 속도(이동량) 계산
                    // 이 값이 곧 공을 미는 속도가 됩니다.
                    strikerVx = event.x - lastTouchX
                    strikerVy = event.y - lastTouchY

                    // 2. 스트라이커 위치 이동
                    strikerX = event.x
                    strikerY = event.y

                    // 3. 충돌 체크 (여기서는 '밀기' 물리만 적용)
                    checkCollisionWithBalls()

                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
//                currentStrikerSpeed = 0f // 손 떼면 속도 0
//
//                isHoldingStriker = false
//                selectedBallIndex = -1

                isHoldingStriker = false
                strikerVx = 0f
                strikerVy = 0f

                // 만약 이번 터치 중에 '왕먼지'를 밀었다면 -> 2초 뒤 복귀 예약
                if (hasHitBigBall) {
                    isWaitingForReturn = true // 대기 모드 (이제 다른 공 못 건드림)
                    isStrikerLocked = true    // 터치 잠금

                    // 손 뗀 시점으로부터 2초 뒤에 복귀 시작
                    postDelayed({
                        startStrikerReturn()

                        // 왕먼지도 같이 복귀시키기
                        balls.find { it.sizeType == "BIG" }?.let { bigBall ->
                            // 이미 골인해서 사라진 상태가 아니라면 복귀시킴
                            if (!bigBall.isGoal) {
                                bigBall.isReturning = true
                                bigBall.vx = 0f
                                bigBall.vy = 0f
                            }
                        }

                        isWaitingForReturn = false
                    }, 2000)
                }
            }
        }
        return true
    }

    private fun checkCollisionWithBalls() {
        // 복귀 대기 중이면 무시
        if (isStrikerReturning || isWaitingForReturn) return

        for (ball in balls) {
            if (ball.isGoal || ball.isReturning) continue

            val dx = ball.x - strikerX
            val dy = ball.y - strikerY
            val dist = hypot(dx, dy)

            // 히트박스 비율 (0.7 ~ 0.8 추천)
            val hitBoxRatio = 0.7f
            val collisionDist = (ball.radius + strikerRadius) * hitBoxRatio

            // 충돌 발생!
            if (dist < collisionDist) {

                // 왕먼지(BIG)가 아니면 통과
                if (ball.sizeType != "BIG") continue

                // [플래그 ON] "나 지금 공 밀고 있어!"
                hasHitBigBall = true

                // ================================================================
                // [수정] 밀대 효과 (Push Physics) - 튕겨내기 로직 삭제
                // ================================================================

                // 1. 겹침 방지 (Position Projection)
                // 스트라이커 안으로 들어온 만큼, 공을 바깥으로 밀어내서 딱 붙어있게 만듦
                val overlap = collisionDist - dist
                val angle = atan2(dy, dx)

                ball.x += cos(angle) * overlap
                ball.y += sin(angle) * overlap

                // 2. 속도 전달 (Velocity Transfer)
                // 내 손의 속도(strikerVx, Vy)를 공에게 그대로 덮어씌움
                // 이러면 손을 뗄 때 마지막 속도로 공이 굴러갑니다.
                ball.vx = strikerVx
                ball.vy = strikerVy

                // (선택) 손을 떼면 좀 더 시원하게 굴러가라고 가속도 살짝 추가 가능
                // ball.vx = strikerVx * 1.2f
                // ball.vy = strikerVy * 1.2f

                // [중요] 여기서는 복귀 타이머(postDelayed)를 실행하지 않음!
                // 터치가 끝날 때(ACTION_UP) 실행함.
                // break는 둬도 되고 빼도 됨 (큰 공 하나뿐이라면 break 추천)
                break
            }
        }
    }

    // 스트라이커 복귀 함수
    private fun startStrikerReturn() {
        if (isStrikerReturning) return

        isStrikerReturning = true
//        isStrikerLocked = true // 터치 잠금

        // 기회 차감
//        currentChances--

        // 토스트 등으로 남은 기회 알려주기 (선택)
        // Toast.makeText(context, "남은 기회: $currentChances", Toast.LENGTH_SHORT).show()
    }

    private fun checkBallToBallCollisions() {
        // 이중 반복문으로 모든 공끼리 비교
        for (i in 0 until balls.size) {
            for (j in i + 1 until balls.size) {
                val b1 = balls[i]
                val b2 = balls[j]

                // 이미 골인했거나 복귀 중인 공은 충돌 무시
                if (b1.isGoal || b1.isReturning || b2.isGoal || b2.isReturning) continue

                val dx = b2.x - b1.x
                val dy = b2.y - b1.y
                val dist = hypot(dx, dy)

                // 두 공의 반지름 합보다 거리가 가까우면 충돌!
//                val minDist = b1.radius + b2.radius


                val hitBoxRatio = 0.5f
                val minDist = (b1.radius + b2.radius) * hitBoxRatio

                if (dist < minDist) {
                    // ------------------------------------------------
                    // 1. 위치 보정 (겹침 방지)
                    // ------------------------------------------------
                    // 공이 겹쳐 있으면 서로 밀어내서 딱 붙게 만듦
                    val angle = atan2(dy, dx)
                    val overlap = minDist - dist

                    // 질량에 반비례해서 밀려남 (가벼운 애가 더 많이 밀림)
                    val totalMass = b1.mass + b2.mass
                    val m1Ratio = b2.mass / totalMass
                    val m2Ratio = b1.mass / totalMass

                    val moveX = cos(angle) * overlap
                    val moveY = sin(angle) * overlap

                    b1.x -= moveX * m1Ratio
                    b1.y -= moveY * m1Ratio
                    b2.x += moveX * m2Ratio
                    b2.y += moveY * m2Ratio

                    // ------------------------------------------------
                    // 2. 속도 교환 (충돌 물리 공식 - 2D 탄성 충돌)
                    // ------------------------------------------------
                    // 충돌 면의 법선 벡터(Normal Vector)
                    val nx = dx / dist
                    val ny = dy / dist

                    // 접선 벡터(Tangent Vector)
                    val tx = -ny
                    val ty = nx

                    // 법선 방향(충돌 방향) 속도 성분 (Dot Product)
                    val v1n = b1.vx * nx + b1.vy * ny
                    val v1t = b1.vx * tx + b1.vy * ty
                    val v2n = b2.vx * nx + b2.vy * ny
                    val v2t = b2.vx * tx + b2.vy * ty

                    // 충돌 후 법선 방향 속도 공식 (운동량 보존)
                    // (m1 - m2) * v1 + 2 * m2 * v2 / (m1 + m2)
                    val v1nAfter = ((b1.mass - b2.mass) * v1n + 2 * b2.mass * v2n) / totalMass
                    val v2nAfter = ((b2.mass - b1.mass) * v2n + 2 * b1.mass * v1n) / totalMass

                    // 탄성(Elasticity) 적용 (두 공 중 더 낮은 탄성 따라감)
                    val e = min(b1.elasticity, b2.elasticity)

                    // 최종 속도 계산
                    b1.vx = (v1nAfter * nx + v1t * tx) * e // 탄성 적용
                    b1.vy = (v1nAfter * ny + v1t * ty) * e
                    b2.vx = (v2nAfter * nx + v2t * tx) * e
                    b2.vy = (v2nAfter * ny + v2t * ty) * e
                }
            }
        }
    }

    // --- Fling (던지기) 처리 ---
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (selectedBallIndex != -1) {
            // 잡고 있던 공에 속도 부여
            val ball = balls[selectedBallIndex]
            ball.vx = velocityX / 25 // 속도 조절
            ball.vy = velocityY / 25

            selectedBallIndex = -1 // 손 놓음 처리
            invalidate()
        }
        return true
    }

//    // --- GestureDetector 필수 구현부 ---
//    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
//        if (isDragging) {
//            // 손을 튕겼을 때 속도 적용 (값이 너무 크므로 조절을 위해 30으로 나눔)
//            this.velocityX = velocityX / 30
//            this.velocityY = velocityY / 30
//            invalidate() // 애니메이션 시작
//        }
//        return true
//    }

    // 사용하지 않는 제스처 메서드들 (빈 상태로 둠)
    override fun onDown(e: MotionEvent): Boolean = true
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean = false
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean = false
    override fun onLongPress(e: MotionEvent) {}
}