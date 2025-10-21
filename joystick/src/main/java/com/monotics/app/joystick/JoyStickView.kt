package com.monotics.app.joystick

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

class JoyStickView(context: Context?, attrs: AttributeSet?): View(context, attrs), Runnable {
    private val basePaint: Paint = Paint()
    private val stickPaint: Paint = Paint()
    private var baseColor: Int = 0
    private var stickColor: Int = 0
    private var moveListener: OnMoveListener? = null
    private var useSpring = true
    private var mThread = Thread(this)
    private var moveUpdateInterval = DEFAULT_UPDATE_INTERVAL

    private var mPosX: Float = 0.0f
    private var mPosY: Float = 0.0f

    private var mCenterX: Float = 0.0f
    private var mCenterY: Float = 0.0f

    private var stickRatio = 0.0f
    private var baseRatio = 0.0f

    private var stickRadius = 0.0f
    private var baseRadius = 0.0f


    init {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.Joystick,
            0, 0
        )?.apply {

            try {
                baseColor = getColor(R.styleable.Joystick_joystickBaseColor, Color.YELLOW)
                stickColor = getColor(R.styleable.Joystick_joystickStickColor, Color.BLUE)
                stickRatio = getFraction(R.styleable.Joystick_joystickStickRatio, 1, 1, 0.25f)
                baseRatio = getFraction(R.styleable.Joystick_joystickBaseRatio, 1, 1, 0.75f)
                useSpring = getBoolean(R.styleable.Joystick_joystickUseSpring, true)
            } finally {
                recycle()
            }

            basePaint.isAntiAlias = true
            basePaint.color = baseColor
            basePaint.style = Paint.Style.FILL

            stickPaint.isAntiAlias = true
            stickPaint.color = stickColor
            stickPaint.style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPosX = (width / 2).toFloat()
        mPosY = (width / 2).toFloat()
        mCenterX = mPosX
        mCenterY = mPosY

        val d = w.coerceAtMost(h)
        stickRadius = d / 2 * stickRatio
        baseRadius = d / 2 * baseRatio
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mPosX = event!!.x
        mPosY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mThread.isAlive)
                    mThread.interrupt()
                mThread = Thread(this)
                mThread.start()
                moveListener?.onMove(getAngle(), getStrength())
            }
            MotionEvent.ACTION_UP -> {
                mThread.interrupt()
                if (useSpring) {
                    mPosX = mCenterX
                    mPosY = mCenterY
                    moveListener?.onMove(getAngle(), getStrength())
                }
            }
        }

        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))

        if (length > baseRadius) {
            // length:radius = (mPosX - mCenterX):new mPosX
            // length:radius = (mPosY - mCenterY):new mPosY
            mPosX = (mPosX - mCenterX) * baseRadius / length + mCenterX
            mPosY = (mPosY - mCenterY) * baseRadius / length + mCenterY
        }

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas?.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), baseRadius, basePaint)
        canvas?.drawCircle(mPosX, mPosY, stickRadius, stickPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // setting the measured values to resize the view to a certain width and height
        val d: Int = measure(widthMeasureSpec).coerceAtMost(measure(heightMeasureSpec))
        setMeasuredDimension(d, d)
    }

    private fun measure(measureSpec: Int): Int {
        return if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
            // if no bounds are specified return a default size (200)
            DEFAULT_SIZE
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            MeasureSpec.getSize(measureSpec)
        }
    }

    private fun getAngle(): Int {
        val xx = mPosX - mCenterX
        val yy = mCenterY - mPosY
        val angle = Math.toDegrees(atan2(yy, xx).toDouble()).toInt()
        // The bottom of the horizontal line is a negative value.
        return if (angle < 0) angle + 360 else angle
    }

    private fun getStrength(): Int {
        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))
        return (length / baseRadius * 100).toInt()
    }


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

    fun setOnMoveListener(listener: OnMoveListener, intervalMs: Int) {
        moveListener = listener
        moveUpdateInterval = intervalMs
    }

    fun setOnMoveListener(listener: OnMoveListener) {
        setOnMoveListener(listener, DEFAULT_UPDATE_INTERVAL)
    }

    fun interface OnMoveListener {
        fun onMove(x: Int, y: Int)
    }

    companion object {
//        private val TAG = JoystickView::class.java.simpleName
        private const val DEFAULT_SIZE = 200
        private const val DEFAULT_UPDATE_INTERVAL = 50
    }


//    // 조이스틱 그리기
//    private val outerPaint: Paint = Paint()
//    private val innerPaint: Paint = Paint()
//
//    // 조이스틱 색상
//    private var outerColor: Int = 0
//    private var innerColor: Int = 0
//
//    // 조이스틱 움직임 감지 리스너
//    private var moveListener: OnMoveListener? = null
//
//    // 조이스틱 릴리즈 시 스프링 효과를 사용할지 여부를 나타내는 플래그
//    private var useSpring = true
//
//    // 조이스틱 움직임 업데이트를 위한 스레드
//    private var mThread = Thread(this)
//
//    // 조이스틱 움직임 업데이트 간격 (밀리초)
//    private var moveUpdateInterval = DEFAULT_UPDATE_INTERVAL
//
//    // 조이스틱 x, y 위치
//    private var mPosX: Float = 0.0f
//    private var mPosY: Float = 0.0f
//
//    // 조이스틱 중심 x, y 위치
//    private var mCenterX: Float = 0.0f
//    private var mCenterY: Float = 0.0f
//
//    // 조이스틱의 내부, 외부 원의 비율과 반지름
//    private var innerRatio = 0.0f
//    private var outerRatio = 0.0f
//    private var innerRadius = 0.0f
//    private var outerRadius = 0.0f
//
//    init {
//        // XML에서 정의된 사용자 지정 속성을 얻기 위한 코드
//        context?.theme?.obtainStyledAttributes(
//            attrs,
//            R.styleable.Joystick,
//            0, 0
//        )?.apply {
//
//            try {
//                outerColor = getColor(R.styleable.Joystick_joystickOuterColor, Color.YELLOW)
//                innerColor = getColor(R.styleable.Joystick_joystickInnerColor, Color.BLUE)
//                innerRatio = getFraction(R.styleable.Joystick_joystickInnerRatio, 1, 1, 0.25f)
//                outerRatio = getFraction(R.styleable.Joystick_joystickOuterRatio, 1, 1, 0.75f)
//                useSpring = getBoolean(R.styleable.Joystick_joystickUseSpring, true)
//            } finally {
//                recycle()
//            }
//
//            outerPaint.isAntiAlias = true
//            outerPaint.color = outerColor
//            outerPaint.style = Paint.Style.FILL
//
//            innerPaint.isAntiAlias = true
//            innerPaint.color = innerColor
//            innerPaint.style = Paint.Style.FILL
//        }
//    }
//
//    // View 크기 변경 시, 호출 함수
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        // 초기 조이스틱, 중심 위치 설정
//        mPosX = (width / 2).toFloat()
//        mPosY = (width / 2).toFloat()
//        mCenterX = mPosX
//        mCenterY = mPosY
//
//        // 조이스틱의 내부, 외부 반지름 계산
//        val d = w.coerceAtMost(h)
//        innerRadius = d / 2 * innerRatio
//        outerRadius = d / 2 * outerRatio
//    }
//
//    // 터치 이벤트 처리 함수
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        mPosX = event!!.x
//        mPosY = event.y
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                // 스레드 실행 중, 중단 및 새로 시작
//                if (mThread.isAlive)
//                    mThread.interrupt()
//                mThread = Thread(this)
//                mThread.start()
//                moveListener?.onMove(getAngle(), getStrength())
//            }
//            MotionEvent.ACTION_UP -> {
//                // 스레드 중단 및 스프링 효과 적용
//                mThread.interrupt()
//                if (useSpring) {
//                    mPosX = mCenterX
//                    mPosY = mCenterY
//                    moveListener?.onMove(getAngle(), getStrength())
//                }
//            }
//        }
//
//        // 조이스틱이 외부 원을 벗어나지 않도록 조정
//        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))
//        if (length > outerRadius) {
//            // length:radius = (mPosX - mCenterX):new mPosX
//            // length:radius = (mPosY - mCenterY):new mPosY
//            mPosX = (mPosX - mCenterX) * outerRadius / length + mCenterX
//            mPosY = (mPosY - mCenterY) * outerRadius / length + mCenterY
//        }
//
//        // 화면 다시 그리기 요청
//        invalidate()
//        return true
//    }
//
//    // View 그리는 함수
//    override fun onDraw(canvas: Canvas) {
//        canvas?.drawCircle((width / 2).toFloat(), (width / 2).toFloat(), outerRadius, outerPaint)
//        canvas?.drawCircle(mPosX, mPosY, innerRadius, innerPaint)
//    }
//
//    // View 크기 측정 함수
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        // setting the measured values to resize the view to a certain width and height
//        val d: Int = measure(widthMeasureSpec).coerceAtMost(measure(heightMeasureSpec))
//        setMeasuredDimension(d, d)
//    }
//
//    // View 크기 측정을 돕는 함수
//    private fun measure(measureSpec: Int): Int {
//        return if (MeasureSpec.getMode(measureSpec) == MeasureSpec.UNSPECIFIED) {
//            // 경계가 지정되지 않은 경우 기본 크기 반환 (200)
//            DEFAULT_SIZE
//        } else {
//            // 사용 가능한 공간을 채우도록 항상 전체 크기 반환
//            MeasureSpec.getSize(measureSpec)
//        }
//    }
//
//    // 조이스틱 각도 계산 함수
//    private fun getAngle(): Int {
//        val xx = mPosX - mCenterX
//        val yy = mCenterY - mPosY
//        val angle = Math.toDegrees(atan2(yy, xx).toDouble()).toInt()
//        // 수평선 아래는 음수 값 계산
//        return if (angle < 0) angle + 360 else angle
//    }
//
//    // 조이스틱 강도 계산 함수
//    private fun getStrength(): Int {
//        val length = sqrt((mPosX - mCenterX).pow(2) + (mPosY - mCenterY).pow(2))
//        return (length / outerRadius * 100).toInt()
//    }
//
//    // 주기적으로 실행되는 스레드
//    override fun run() {
//        while (!Thread.interrupted()) {
//            post {
//                moveListener?.onMove(getAngle(), getStrength())
//            }
//            try {
//                Thread.sleep(moveUpdateInterval.toLong())
//            } catch (e: InterruptedException) {
//                break
//            }
//        }
//    }
//
//    // 조이스틱 움직임 리스너 설정 함수 (리스너 및 업데이트 간격)
//    fun setOnMoveListener(listener: OnMoveListener, intervalMs: Int) {
//        moveListener = listener
//        moveUpdateInterval = intervalMs
//    }
//
//    // 조이스틱 움직임 리스너 설정
//    fun setOnMoveListener(listener: OnMoveListener) {
//        setOnMoveListener(listener, DEFAULT_UPDATE_INTERVAL)
//    }
//
//    // 조이스틱 움직임 리스너 인터페이스
//    fun interface OnMoveListener {
//        fun onMove(x: Int, y: Int)
//    }
//
//    // 상수 및 태그
//    companion object {
////        private val TAG = JoystickView::class.java.simpleName
//        private const val DEFAULT_SIZE = 200
//        private const val DEFAULT_UPDATE_INTERVAL = 50
//    }
//
//    // 조이스틱 각도 설정 함수
//    fun setAngle(angle: Int) {
//        val radian = Math.toRadians(angle.toDouble())
//        // 각도와 현재 조이스틱의 강도(반지름)를 사용하여 새로운 조이스틱 위치를 계산
//        val newX = mCenterX + getStrength() / 100f * outerRadius * cos(radian)
//        val newY = mCenterY - getStrength() / 100f * outerRadius * sin(radian)
//        updateJoystickPosition(newX.toFloat(), newY.toFloat())
//    }
//
//    // 조이스틱 강도 설정 함수
//    fun setStrength(strength: Int) {
//        // 강도를 비율로 변환 (0 ~ 100 -> 0 ~ 1)
//        val ratio = strength / 100f
//        // 새로운 조이스틱 위치를 계산하고 업데이트
//        val newX = mCenterX + (mPosX - mCenterX) * ratio
//        val newY = mCenterY + (mPosY - mCenterY) * ratio
//        updateJoystickPosition(newX, newY)
//    }
//
//    // 조이스틱 위치 업데이트 함수
//    private fun updateJoystickPosition(newX: Float, newY: Float) {
//        mPosX = newX
//        mPosY = newY
//        // 화면 다시 그리기 요청
//        invalidate()
//    }


}