package com.example.thoughts_cleaning.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.thoughts_cleaning.api.model.SlideGameBall
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

class GameSlideView(context: Context, attrs: AttributeSet? = null) : View(context, attrs), GestureDetector.OnGestureListener {

    init {
        // 그림자 효과 및 부드러운 그라데이션을 위해 소프트웨어 렌더링 사용
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    // 1. 그래픽 도구
    private val goalPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.FILL; isAntiAlias = true }
//    private val goalTextPaint = Paint().apply { color = Color.WHITE; textSize = 60f; textAlign = Paint.Align.CENTER }
    private val ballPaint = Paint().apply { isAntiAlias = true }

    // 골대 입구 테두리 선 (입체감 용)
    private val goalBorderPaint = Paint().apply { color = Color.rgb(50, 50, 50); style = Paint.Style.STROKE; strokeWidth = 8f; isAntiAlias = true }

    // 2. 게임 오브젝트
    private val balls = mutableListOf<SlideGameBall>() // 공 5개를 담을 리스트
    private val ballRadius = 40f

    // 스트라이커 변수
    private var strikerX = 0f
    private var strikerY = 0f
    private val strikerRadius = 60f // 스트라이커는 좀 더 큼
    private var isHoldingStriker = false

    // 상단 골인 지점 (넓은 공간)
    private val goalRect = RectF()
    private val goalHeightRatio = 0.1f // 화면 상단 20%를 골인 지점으로


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

    private val ballTextPaint = Paint().apply {
        color = Color.WHITE // 글자색 (흰색)
        textSize = 45f      // 글자 크기
        textAlign = Paint.Align.CENTER // 좌우 중앙 정렬
        typeface = Typeface.DEFAULT_BOLD // 굵은 글씨
        isAntiAlias = true
    }

//    private var ballLabel = ""
    private val targetLabels = listOf("A", "B", "C", "D", "E")
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

    fun setBallData(content: String, kindName: String, kindDetail: String) {
        this.targetLabel = kindName

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
                text = targetLabel, // 여기서 저장된 글자를 사용
                startX = bx,
                startY = by
            ))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 상단 골인 영역 설정 (전체 너비, 상단 20%)

        // 목표지점 너비: 화면 너비의 절반 (50%)
        val goalWidth = w * 0.5f
        // 왼쪽 시작점: (전체너비 - 목표너비) / 2 = 중앙 정렬
        val goalLeft = (w - goalWidth) / 2f
        val goalRight = goalLeft + goalWidth

        // 상단 중앙에 배치 (Top: 0f, Bottom: 상단 20% 지점)
        goalRect.set(goalLeft, 0f, goalRight, h * goalHeightRatio)

        if (targetLabels.isNotEmpty()) {
            initBalls(w, h)
        }

        // 스트라이커 초기 위치 (화면 하단 중앙)
        strikerX = w / 2f
        strikerY = h * 0.85f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. 배경을 약간 어둡게 (게임판 느낌)
        canvas.drawColor(0xFFEEEEEE.toInt())

        // 1. 상단 골인 영역 그리기
        canvas.drawRect(goalRect, goalPaint)

//        canvas.drawLine(goalRect.left, goalRect.bottom, goalRect.right, goalRect.bottom, goalBorderPaint)
//        canvas.drawText("GOAL", width / 2f, goalRect.centerY(), goalTextPaint)

        canvas.drawLine(goalRect.left, goalRect.bottom, goalRect.right, goalRect.bottom, goalBorderPaint)

        // 2. 공 5개 그리기
        for (ball in balls) {
            ballPaint.color = ball.color

            // [수정] 투명도 설정 로직 변경
            if (ball.isGoal) {
                ballPaint.alpha = 150 // 골인되면 약간 투명
            } else if (ball.isReturning) {
                ballPaint.alpha = 100 // [추가] 복귀 중일 때는 더 투명하게 (유령처럼)
            } else {
                ballPaint.alpha = 255 // 평소엔 불투명
            }

            canvas.drawCircle(ball.x, ball.y, ballRadius, ballPaint)

            // 글자의 높이 중심을 구해서 공의 중심(y)에 맞춤
            val textY = ball.y - ((ballTextPaint.descent() + ballTextPaint.ascent()) / 2)
            canvas.drawText(ball.text, ball.x, textY, ballTextPaint)

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

            // ----------------------------------------------------------------
            // 1. [복귀 모드] 원래 자리로 미끄러져 내려가는 로직
            // ----------------------------------------------------------------
            if (ball.isReturning) {
                // 목표 지점(원래 위치)까지의 거리 계산
                val dx = ball.startX - ball.x
                val dy = ball.startY - ball.y
                val dist = hypot(dx, dy)

                val speed = 25f

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
//                    val speed = 25f // 돌아오는 속도 (조절 가능)
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


            // 이미 골인된 공이나, 사용자가 잡고 있는 공은 물리연산 제외
            if (ball.isGoal || (balls.indexOf(ball) == selectedBallIndex)) continue

            // 속도 적용
            ball.x += ball.vx
            ball.y += ball.vy

            // 2. 마찰력 적용 (골인 상태에 따라 다르게 적용)
            if (ball.isGoal) {
                // [핵심] 골대 안에서는 훨씬 강한 마찰력을 적용하여 금방 멈추게 함
                ball.vx *= goalFriction
                ball.vy *= goalFriction
            } else {
                // 일반 필드 마찰력
                ball.vx *= friction
                ball.vy *= friction
            }

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

            // 상단 영역 처리
            if (ball.y - ballRadius < goalRect.bottom) {
                // 공이 골대 라인보다 위로 올라왔을 때

                // A. 골인 영역(가로 범위) 안에 있는가?
                if (ball.x > goalRect.left && ball.x < goalRect.right) {
                    // B. 아직 골인 판정이 안 났고, 위로 올라가는 중이라면 골인 처리 시작
                    if (!ball.isGoal && ball.vy < 0) {
                        ball.isGoal = true
                        // Toast.makeText(context, "골인!", Toast.LENGTH_SHORT).show() // 너무 자주 떠서 주석처리
                    }
                    // C. 골인 상태라면 상단 벽(화면 끝)에 부딪혀 멈추게 함
                    if (ball.y - ballRadius < 0) {
                        ball.y = ballRadius
                        ball.vy = 0f // 상단 벽에 닿으면 수직 속도 제거
                    }
                }
                // D. 골대 옆 빈 벽에 맞은 경우 (빗나감)
                else if (!ball.isGoal) {
                    ball.y = goalRect.bottom + ballRadius // 라인 밖으로 밀어냄
                    ball.vy = -ball.vy * 0.9f // 튕겨 나옴
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
                            // 리스너 호출 등...
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