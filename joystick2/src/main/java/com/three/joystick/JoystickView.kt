package com.three.joystick

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class JoystickView(context: Context?, attrs: AttributeSet?): View(context, attrs), Runnable {
    // 조이스틱 그리기
    private val outerPaint: Paint = Paint()
    private val innerPaint: Paint = Paint()

    // 조이스틱 색상
    private var outerColor: Int = 0
    private var innerColor: Int = 0

    // 조이스틱 움직임 감지 리스너
    private var moveListener: OnMoveListener? = null

    // 조이스틱 릴리즈 시 스프링 효과를 사용할지 여부를 나타내는 플래그
    private var useSpring = true

    // 조이스틱 움직임 업데이트를 위한 스레드
    private var mThread = Thread(this)

    // 조이스틱 움직임 업데이트 간격 (밀리초)
    private var moveUpdateInterval = DEFAULT_UPDATE_INTERVAL

    // 조이스틱 x, y 위치
    private var mPosX: Float = 0.0f
    private var mPosY: Float = 0.0f

    // 조이스틱 중심 x, y 위치
    private var mCenterX: Float = 0.0f
    private var mCenterY: Float = 0.0f

    // 조이스틱의 내부, 외부 원의 비율과 반지름
    private var innerRatio = 0.0f
    private var outerRatio = 0.0f
    private var innerRadius = 0.0f
    private var outerRadius = 0.0f

    init {
        // XML에서 정의된 사용자 지정 속성을 얻기 위한 코드
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.Joystick,
            0, 0
        )?.apply {

            try {
                outerColor = getColor(R.styleable.Joystick_joystickOuterColor, Color.YELLOW)
                innerColor = getColor(R.styleable.Joystick_joystickInnerColor, Color.BLUE)
                innerRatio = getFraction(R.styleable.Joystick_joystickInnerRatio, 1, 1, 0.25f)
                outerRatio = getFraction(R.styleable.Joystick_joystickOuterRatio, 1, 1, 0.75f)
                useSpring = getBoolean(R.styleable.Joystick_joystickUseSpring, true)
            } finally {
                recycle()
            }

            outerPaint.isAntiAlias = true
            outerPaint.color = outerColor
            outerPaint.style = Paint.Style.FILL

            innerPaint.isAntiAlias = true
            innerPaint.color = innerColor
            innerPaint.style = Paint.Style.FILL
        }
    }

    // View 크기 변경 시, 호출 함수
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 초기 조이스틱, 중심 위치 설정
        mPosX = (width / 2).toFloat()
        mPosY = (width / 2).toFloat()
        mCenterX = mPosX
        mCenterY = mPosY

        // 조이스틱의 내부, 외부 반지름 계산
        val d = w.coerceAtMost(h)
        innerRadius = d / 2 * innerRatio
        outerRadius = d / 2 * outerRatio
    }

    // 터치 이벤트 처리 함수
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mPosX = event!!.x
        mPosY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 스레드 실행 중, 중단 및 새로 시작
                if (mThread.isAlive)
                    mThread.interrupt()
                mThread = Thread(this)
                mThread.start()
                moveListener?.onMove(getAngle(), getStrength())
            }
            MotionEvent.ACTION_UP -> {
                // 스레드 중단 및 스프링 효과 적용
                mThread.interrupt()
                if (useSpring) {
                    mPosX = mCenterX
                    mPosY = mCenterY
                    moveListener?.onMove(getAngle(), getStrength())
                }
            }
        }

        // 조이스틱이 외부 원을 벗어나지 않도록 조정
        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))
        if (length > outerRadius) {
            // length:radius = (mPosX - mCenterX):new mPosX
            // length:radius = (mPosY - mCenterY):new mPosY
            mPosX = (mPosX - mCenterX) * outerRadius / length + mCenterX
            mPosY = (mPosY - mCenterY) * outerRadius / length + mCenterY
        }

        // 화면 다시 그리기 요청
        invalidate()
        return true
    }

    // View 그리는 함수
    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), outerRadius, outerPaint)
        canvas.drawCircle(mPosX, mPosY, innerRadius, innerPaint)
    }

    // View 크기 측정 함수
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // setting the measured values to resize the view to a certain width and height
        val d: Int = measure(widthMeasureSpec).coerceAtMost(measure(heightMeasureSpec))
        setMeasuredDimension(d, d)
    }

    // View 크기 측정을 돕는 함수
    private fun measure(measureSpec: Int): Int {
        return if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            // 경계가 지정되지 않은 경우 기본 크기 반환 (200)
            DEFAULT_SIZE
        } else {
            // 사용 가능한 공간을 채우도록 항상 전체 크기 반환
            MeasureSpec.getSize(measureSpec)
        }
    }

    // 조이스틱 각도 계산 함수
//    private fun getAngle(): Int {
//        val xx = mPosX - mCenterX
//        val yy = mCenterY - mPosY
//        val angle = Math.toDegrees(atan2(yy, xx).toDouble()).toInt()
//        // 수평선 아래는 음수 값 계산
//        return if (angle < 0) angle + 360 else angle
//    }
    private fun getAngle(): Float {
        val xx = mPosX - mCenterX
        val yy = mPosY - mCenterY

        // atan2의 결과(라디안)를 실수형(Double)으로 계산하고, toDegrees로 각도(Double)로 변환합니다.
        val angleInDegrees = Math.toDegrees(kotlin.math.atan2(yy.toDouble(), xx.toDouble()))

        // 각도를 Float로 변환하여 저장합니다.
        var angle = angleInDegrees.toFloat()

        // 0도에서 360도 사이의 양수 값으로 변환합니다.
        // 수평선 아래(시계 반대 방향으로 180도 ~ 360도)는 음수 값이 나오므로 360을 더합니다.
        if (angle < 0) {
            angle += 360f
        }

        return angle
    }


    // 조이스틱 강도 계산 함수
//    private fun getStrength(): Int {
//        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))
//        return (length / outerRadius * 100).toInt()
//    }

    private fun getStrength(): Float {
        // 1. 조이스틱 중심으로부터 현재 위치까지의 거리 (대각선 길이)를 Float로 계산
        val length = kotlin.math.sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))

        // 2. 길이를 최대 반지름(outerRadius)으로 나누어 비율을 구하고 100을 곱하여 백분율(0.0 ~ 100.0)을 계산
        // outerRadius가 Float이므로 결과는 자동으로 Float이 됩니다.
        val strength = length / outerRadius * 100f

        // 3. Float 타입의 강도(Strength) 값을 반환
        return strength
    }

    // 주기적으로 실행되는 스레드
    override fun run() {
        while (!Thread.interrupted()) {
            post {
                moveListener?.onMove(getAngle(), getStrength())
            }
            try {
                Thread.sleep(moveUpdateInterval.toLong())
            } catch (e: InterruptedException) {
                break
            }
        }
    }

    // 조이스틱 움직임 리스너 설정 함수 (리스너 및 업데이트 간격)
    fun setOnMoveListener(listener: OnMoveListener, intervalMs: Int) {
        moveListener = listener
        moveUpdateInterval = intervalMs
    }

    // 조이스틱 움직임 리스너 설정
    fun setOnMoveListener(listener: OnMoveListener) {
        setOnMoveListener(listener, DEFAULT_UPDATE_INTERVAL)
    }

    // 조이스틱 움직임 리스너 인터페이스
    fun interface OnMoveListener {
        fun onMove(x: Float, y: Float)
    }

    // 상수 및 태그
    companion object {
        private val TAG = JoystickView::class.java.simpleName
        private const val DEFAULT_SIZE = 200
        const val DEFAULT_UPDATE_INTERVAL = 50
    }

    // 조이스틱 각도 설정 함수
    fun setAngle(angle: Int) {
        val radian = Math.toRadians(angle.toDouble())
        // 각도와 현재 조이스틱의 강도(반지름)를 사용하여 새로운 조이스틱 위치를 계산
        val newX = mCenterX + getStrength() / 100f * outerRadius * cos(radian)
        val newY = mCenterY - getStrength() / 100f * outerRadius * sin(radian)
        updateJoystickPosition(newX.toFloat(), newY.toFloat())
    }

    // 조이스틱 강도 설정 함수
    fun setStrength(strength: Int) {
        // 강도를 비율로 변환 (0 ~ 100 -> 0 ~ 1)
        val ratio = strength / 100f
        // 새로운 조이스틱 위치를 계산하고 업데이트
        val newX = mCenterX + (mPosX - mCenterX) * ratio
        val newY = mCenterY + (mPosY - mCenterY) * ratio
        updateJoystickPosition(newX, newY)
    }

    // 조이스틱 위치 업데이트 함수
    private fun updateJoystickPosition(newX: Float, newY: Float) {
        mPosX = newX
        mPosY = newY
        // 화면 다시 그리기 요청
        invalidate()
    }
}