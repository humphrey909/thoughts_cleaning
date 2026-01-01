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
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.api.model.SlideGameBall
import com.example.thoughts_cleaning.api.response.ResThoughtOfUserListDto
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

class GameSlideView(context: Context, attrs: AttributeSet? = null) : View(context, attrs), GestureDetector.OnGestureListener {
    // 1. 그래픽 도구
    private val goalPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.FILL; isAntiAlias = true }
    private val goalTextPaint = Paint().apply { color = Color.WHITE; textSize = 60f; textAlign = Paint.Align.CENTER }
    private val ballPaint = Paint().apply { isAntiAlias = true }

    // 골대 입구 테두리 선 (입체감 용)
    private val goalBorderPaint = Paint().apply { color = Color.rgb(50, 50, 50); style = Paint.Style.STROKE; strokeWidth = 8f; isAntiAlias = true }

    // 2. 게임 오브젝트
    private val balls = mutableListOf<SlideGameBall>() // 공 5개를 담을 리스트
    private val ballRadius = 80f

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

    private val ballBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.img_dust)
    private lateinit var ballRect:RectF

    private var entranceCleanBitmap: Bitmap? = null

    private val ballTextPaint = Paint().apply {
        color = Color.BLACK // 글자색 (흰색)
        textSize = 45f      // 글자 크기
        textAlign = Paint.Align.CENTER // 좌우 중앙 정렬
        typeface = Typeface.DEFAULT_BOLD // 굵은 글씨
        isAntiAlias = true
    }

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

    init {
        // 그림자 효과 및 부드러운 그라데이션을 위해 소프트웨어 렌더링 사용
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    interface OnGameEndListener {
        fun onGameOver(successText: String)
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

        targetLabels = contentList.thoughtsList.map { it.contentThought }

        if (width > 0 && height > 0) {
            initBalls(width, height)
            invalidate()
        }
    }

    private fun initBalls(w: Int, h: Int) {
        balls.clear()

        // 저장된 targetLabels를 사용해서 공 생성
        val count = targetLabels.size
        if (count == 0) return

        val startY = h * 0.8f
        val spacing = w.toFloat() / (count + 1)

        val colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN)

        for (i in 0 until count) {
            val bx = spacing * (i + 1)
            val by = startY

            balls.add(SlideGameBall(
                x = bx, y = by,
                color = colors[i % colors.size],
                text = targetLabels.get(i), // 여기서 저장된 글자를 사용
                startX = bx,
                startY = by
            ))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 상단 골인 영역 설정 (전체 너비, 상단 20%)

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
        strikerX = w / 2f
        strikerY = h * 0.85f
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

            // [수정] 투명도 설정 로직 변경
            if (ball.isGoal) {
                ballPaint.alpha = 150 // 골인되면 약간 투명
            } else if (ball.isReturning) {
                ballPaint.alpha = 100 // [추가] 복귀 중일 때는 더 투명하게 (유령처럼)
            } else {
                ballPaint.alpha = 255 // 평소엔 불투명
            }

            if (ballBitmap != null) {
                aspectRatioCharacter = ballBitmap.width.toFloat() / ballBitmap.height.toFloat()
                val diameter = ballRadius * 2
                newWidthCharacter = diameter
                newHeightCharacter = newWidthCharacter / aspectRatioCharacter

                // 2. 위치 보정: (중심 좌표) - (길이의 절반)
                val left = ball.x - (newWidthCharacter / 2)
                val top = ball.y - (newHeightCharacter / 2)
                val right = left + newWidthCharacter
                val bottom = top + newHeightCharacter

                ballRect = RectF(left, top, right, bottom)
                canvas.drawBitmap(ballBitmap, null, ballRect, null)
            }

            // 글자의 높이 중심을 구해서 공의 중심(y)에 맞춤
            val textY = ball.y - ((ballTextPaint.descent() + ballTextPaint.ascent()) / 2)
            canvas.drawText(ball.text, ball.x, textY, ballTextPaint)

//            if (ball.isReturning || ball.isGoal || abs(ball.vx) > 0.01f || abs(ball.vy) > 0.01f) {
//                isAnyBallMoving = true
//            }

            // 움직임 감지 (골인된 공은 이미 continue 되었으므로 체크 안 함)
            if (ball.isReturning || abs(ball.vx) > 0.01f || abs(ball.vy) > 0.01f) {
                isAnyBallMoving = true
            }
        }

        // 4. 내 스트라이커(채) 그리기
        canvas.drawCircle(strikerX, strikerY, strikerRadius, strikerPaint)
        canvas.drawCircle(strikerX, strikerY, strikerRadius, strikerBorderPaint)

        // 3. 움직이는 공이 하나라도 있으면 물리 업데이트 & 다시 그리기
        if (isAnyBallMoving) {
            updatePhysics()
            invalidate()
        }
    }

    private fun updatePhysics() {
        for (ball in balls) {

            if (ball.isGoal) continue

            // ----------------------------------------------------------------
            // 1. [복귀 모드] 원래 자리로 미끄러져 내려가는 로직
            // ----------------------------------------------------------------
            if (ball.isReturning) {
                // 목표 지점(원래 위치)까지의 거리 계산
                val dx = ball.startX - ball.x
                val dy = ball.startY - ball.y
                val dist = hypot(dx, dy)

                val speed = 10f

                if (dist <= speed) {
                    // 1. 위치를 시작점으로 완벽하게 고정
                    ball.x = ball.startX
                    ball.y = ball.startY

                    // 2. 속도를 0으로 죽임 (확실하게)
                    ball.vx = 0f
                    ball.vy = 0f

                    // 3. 복귀 모드 해제
                    ball.isReturning = false

                    // [추가] 중요! 복귀가 끝난 직후에는 물리 연산을 건너뛰어야
                    // 바로 다음 줄에서 충돌 계산 등으로 다시 튕기는 것을 방지함
                    continue
                } else {
                    // 목표 지점을 향해 일정한 속도로 이동 (Linear Interpolation 느낌)
                    val angle = atan2(dy, dx)

                    ball.x += cos(angle) * speed
                    ball.y += sin(angle) * speed

                    // 돌아오는 중에는 회전이나 물리 충돌 무시 (속도 0 처리)
                    ball.vx = 0f
                    ball.vy = 0f
                }
                // 복귀 중인 공은 아래의 일반 물리 연산을 하지 않고 건너뜀
                continue
            }


            // ----------------------------------------------------------------
            // 2. [골인 모드 - 신규 추가] 빨려 들어감 + 3초 대기
            // ----------------------------------------------------------------
//            if (ball.isGoal) {
//                // A. 빨려 들어가는 연출 (Suction Effect)
//                // 골대의 정중앙 좌표
//                val targetX = goalRect.centerX()
//                val targetY = goalRect.centerY()
//
//                // 현재 위치에서 중앙으로 조금씩 이동 (Lerp: 10%씩 접근)
//                ball.x += (targetX - ball.x) * 0.1f
//                ball.y += (targetY - ball.y) * 0.1f
//
//                // 물리 속도는 0으로 제거 (물리 엔진 간섭 방지)
//                ball.vx = 0f
//                ball.vy = 0f
//
//                // B. 3초 타이머 체크
//                // 현재시간 - 골인시간 > 3000ms (3초)
//                if (System.currentTimeMillis() - ball.goalTime > 3000) {
//                    ball.isGoal = false      // 골인 상태 해제
//                    ball.isReturning = true  // 복귀 모드 시작!
//                    ball.goalTime = 0L       // 시간 초기화
//                }
//
//                // [중요] 골인 상태에서는 아래의 벽 충돌 로직을 실행하지 않음
//                continue
//            }


            // ----------------------------------------------------------------
            // 3. [일반 물리 연산] (이동 및 벽 충돌)
            // ----------------------------------------------------------------

            // 속도 적용
            ball.x += ball.vx
            ball.y += ball.vy

            // 마찰력
            ball.vx *= friction
            ball.vy *= friction

            // 벽 튕기기 (좌우)
            if (ball.x - ballRadius < 0) {
                ball.x = ballRadius
                ball.vx = -ball.vx
            } else if (ball.x + ballRadius > width) {
                ball.x = width - ballRadius
                ball.vx = -ball.vx
            }

            // 벽 튕기기 (하단만, 상단은 골인 지점이라 뚫림)
            if (ball.y + ballRadius > height) {
                ball.y = height - ballRadius
                ball.vy = -ball.vy
            }

            // ----------------------------------------------------------------
            // 4. [골인 감지 로직] 수정됨
            // ----------------------------------------------------------------
            // 상단 영역 (골대 근처) 체크
            if (ball.y - ballRadius < goalRect.bottom) {

//                // 골대 안에 들어왔는지?
//                if (ball.x > goalRect.left && ball.x < goalRect.right &&
//                    ball.y > goalRect.top) { // 상단 벽보다 아래에 있을 때
//
//                    // 아직 골인이 아니고, 위로 올라가는 중이라면 -> 골인 판정!
//                    if (!ball.isGoal && ball.vy < 0) {
//                        ball.isGoal = true
//
//                        // [추가] 현재 시간을 기록 (타이머 시작)
//                        ball.goalTime = System.currentTimeMillis()
//
//                        // 리스너 호출 (점수 획득 등)
////                        goalListener?.onGoal(ball.text)
//                    }
//                }
                // 골대 영역 안에 들어왔는지 확인
                if (ball.x > goalRect.left && ball.x < goalRect.right &&
                    ball.y > goalRect.top) {

                    // 위로 올라가는 중이라면 골인 판정
                    if (!ball.isGoal && ball.vy < 0) {
                        // 1. 현재 공 골인 처리 (상태 변경 및 정지)
                        ball.isGoal = true
                        ball.vx = 0f
                        ball.vy = 0f

                        // 2. [핵심 수정] 모든 공이 골인했는지 검사
                        // "balls 리스트의 모든(all) 아이템이 isGoal == true 인가?"
                        val isAllCleared = balls.all { it.isGoal }

                        // 3. 전부 다 통과했을 때만 액티비티에 알림
                        if (isAllCleared) {
                            // 마지막 공이 들어갔을 때 실행됨
                            // 텍스트는 "모두 해결 완료" 등으로 보내거나, 마지막 공의 텍스트(ball.text)를 보낼 수 있음
                            gameEndListener?.onGameOver("모든 고민이 사라졌습니다!")
                        }
                    }
                }


                // 골대 밖(벽)에 맞았을 때
                else if (ball.y - ballRadius < 0) {
                    ball.y = ballRadius
                    ball.vy = -ball.vy * 0.9f
                    if (!ball.isGoal) ball.isReturning = true
                }
                // 골대 옆/아래 벽 튕기기
                else if (!ball.isGoal && ball.y > goalRect.top) {
                    if (ball.x < goalRect.left || ball.x > goalRect.right) {
                        ball.y = goalRect.bottom + ballRadius
                        ball.vy = -ball.vy * 0.9f
                    }
                }
            }


            // ----------------------------------------------------------------
            // 3. [복귀 트리거] 상단 충돌 및 실패 감지
            // ----------------------------------------------------------------

            // 공이 화면 높이의 50% 위로 올라갔을 때 (ball.y < height * 0.5)
            if (ball.y < height * 0.5f) {

                // CASE A: 상단 벽(천장)에 닿았을 때
                if (ball.y - ballRadius < 0) {
                    // 골인 구간인지 확인
                    if (ball.x > goalRect.left && ball.x < goalRect.right) {
                        // 골인 성공! (기존 로직)
                        if (!ball.isGoal && ball.vy < 0) {
                            ball.isGoal = true
                        }
                        if (ball.y - ballRadius < 0) { ball.y = ballRadius; ball.vy = 0f }
                    }
                    else {
                        // [수정] 골인이 아님(빗나감) -> 복귀 모드 발동!
                        ball.isReturning = true
                    }
                }

                // CASE B: 허공에서 멈췄을 때 (속도가 거의 0이 됨)
                // 골인이 아닌데 위쪽에서 멈춰버리면 게임 진행이 안되므로 복귀시킴
                if (!ball.isGoal && abs(ball.vx) < 1f && abs(ball.vy) < 1f) {
                    ball.isReturning = true
                }
            }
        }
    }

    // --- 터치 이벤트 처리 ---
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 스트라이커를 터치했는지 확인
                val dist = hypot(event.x - strikerX, event.y - strikerY)
                if (dist <= strikerRadius * 1.5) {
                    isHoldingStriker = true
                    lastTouchX = event.x
                    lastTouchY = event.y
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isHoldingStriker) {
                    // 1. 스트라이커 이동
                    strikerX = event.x
                    strikerY = event.y

                    // 2. 내 손가락 속도 계산 (타격감 핵심)
                    strikerVx = event.x - lastTouchX
                    strikerVy = event.y - lastTouchY

                    // 3. 충돌 체크 (공 때리기)
                    checkCollisionWithBalls()

                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                isHoldingStriker = false
//                selectedBallIndex = -1
            }
        }
        return true
    }

    private fun checkCollisionWithBalls() {
        for (ball in balls) {
            if (ball.isGoal) continue

            val dx = ball.x - strikerX
            val dy = ball.y - strikerY
            val dist = hypot(dx, dy)
            val minDist = strikerRadius + ballRadius

            // 충돌 발생!
            if (dist < minDist) {
                // 1. 충돌 각도 계산
                val angle = atan2(dy, dx)

                // 2. 타격 힘 계산 (손가락 속도 + 기본 반발력)
                // 손가락 속도가 너무 느리면 최소한의 힘으로 튕김
                val speed = sqrt(strikerVx * strikerVx + strikerVy * strikerVy)
                val power = if (speed < 10f) 15f else speed * 1.2f

                // 3. 공에 속도 전달 (힘 * 각도)
                ball.vx = (cos(angle) * power).toFloat()
                ball.vy = (sin(angle) * power).toFloat()

                // 4. 공이 스트라이커 안으로 파고들지 않게 강제로 밀어냄 (겹침 방지)
                val pushOutDist = minDist - dist + 1f // 1f는 여유값
                ball.x += (cos(angle) * pushOutDist).toFloat()
                ball.y += (sin(angle) * pushOutDist).toFloat()
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