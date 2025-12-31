package com.example.thoughts_cleaning.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.Log
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.views.game.WindowCleanEvent


class DrawingView (context: Context, attrs: AttributeSet) : View(context, attrs) {

    // 캔버스에 그릴 비트맵과 캔버스 객체
    private lateinit var canvasBitmap: Bitmap
    private lateinit var drawCanvas: Canvas

    // 지우기 경로(Path) 및 페인트(Paint)
    private val erasePath = Path()
    private val erasePaint = Paint()

    // 이미지 파일 로드를 위한 Bitmap
    private var imageBitmap: Bitmap? = null

    // 터치 좌표
    private var mX = 0f
    private var mY = 0f
    private val TOUCH_TOLERANCE = 4f // 터치 움직임 허용 오차

    private var isTouching: Boolean = false

    private var touchImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.clean_window_tool2) // 이미지 리소스
    private var touchX: Float = 0.0F
    private var touchY: Float = 0.0F

    private val offsetX = touchImage.width / 2f
    private val offsetY = touchImage.height / 2f

    init {
        setupDrawing()
    }

    // 2. 이물질 느낌을 낼 페인트 설정
    private val dirtPaint = Paint().apply {
        color = Color.argb(100, 90, 60, 30) // 반투명한 짙은 갈색 (Alpha 100/255)
        isAntiAlias = true
        isDither = true // 색상 단계를 부드럽게
        style = Paint.Style.STROKE // 선으로 그리기
        strokeJoin = Paint.Join.ROUND // 모서리 둥글게
        strokeCap = Paint.Cap.ROUND // 선 끝부분 둥글게
        strokeWidth = 80f // 브러시 두께 (원하는 대로 조절)

        // (선택사항) 가장자리를 뿌옇게 번지게 하여 더 리얼한 먼지 느낌 내기
        // Blur 스타일: NORMAL, SOLID, OUTER, INNER
        maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
    }

    private fun setupDrawing() {
        // 지우개 Paint 설정
        erasePaint.apply {
            isAntiAlias = true
//            isDither = true
            color = Color.TRANSPARENT // 투명색 설정 (지우개 효과를 위해)
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 50f // 지우개 크기
            // 핵심: PorterDuff.Mode.CLEAR 모드는 겹치는 영역을 완전히 투명하게 만듭니다.
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    /**
     * Fragment/Activity에서 호출하여 이미지 설정
     * @param bitmap 캔버스에 그릴 이미지
     */
    fun setImage(bitmap: Bitmap) {
        imageBitmap = bitmap
        // View 크기가 결정된 후 비트맵 초기화를 위해 invalidate 호출
        invalidate()
    }

    // View의 크기가 변경될 때 호출
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            // View 크기에 맞춰 캔버스 비트맵을 생성 (여기에 모든 그림/지우기 작업이 저장됨)
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(canvasBitmap)

            // 이미지 설정이 되어 있으면 캔버스에 이미지 그리기
            imageBitmap?.let {
                // 이미지를 캔버스 크기에 맞게 조정하여 그립니다.
                val srcRect = Rect(0, 0, it.width, it.height)
                val dstRect = Rect(0, 0, w, h)
                drawCanvas.drawBitmap(it, srcRect, dstRect, null)
            }

            // 모든 지우기 작업은 이 drawCanvas에 적용됩니다.
        }
    }

    // 화면에 그리기
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 1. 내부 캔버스 비트맵을 View의 캔버스에 그립니다.
        // 이 비트맵에는 초기 이미지와 지우개 작업이 모두 포함되어 있습니다.
        if (::canvasBitmap.isInitialized) {
            canvas.drawBitmap(canvasBitmap, 0f, 0f, null)
        }

        // 2. 현재 터치 중인 경로를 내부 캔버스 비트맵에 그립니다. (실시간 지우기)
        drawCanvas.drawPath(erasePath, erasePaint)

        // 터치 중일 때만 이미지를 그림
        if (isTouching) {
            canvas.drawBitmap(touchImage, touchX - offsetX, touchY - offsetY - 100f, null)
        }
    }

    // 터치 이벤트 처리 시작
    private fun touchStart(x: Float, y: Float) {
        erasePath.reset()
        erasePath.moveTo(x, y)
        mX = x
        mY = y
    }

    // 터치 이동 처리
    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // 터치 경로를 부드럽게 연결
            erasePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    // 터치 종료 처리
    private fun touchUp() {
        // Path를 내부 캔버스에 그리고 Path를 리셋
        // (onDraw에서 실시간 Path를 그리는 대신, touchUp에서 내부 캔버스에 '확정' 지우기 작업 적용)
        drawCanvas.drawPath(erasePath, erasePaint)
        erasePath.reset()
    }

    // 모든 터치 이벤트 처리
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouching = true

                touchStart(touchX, touchY)
                // View를 다시 그려 onDraw 호출 (실시간 지우개 모양 표시)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(touchX, touchY)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                isTouching = false

                touchUp()
                invalidate()
            }
            else -> return false
        }
        return true
    }

    fun setTouchImage(type:WindowCleanEvent, resourceId: Int){
        Log.d("currentMainFlow", "ENTER_GAME3: ${resourceId}")


        when(type){
            WindowCleanEvent.COMMON -> TODO()
            WindowCleanEvent.NEXT_PAGE -> TODO()
            WindowCleanEvent.QUIT_PAGE -> TODO()
            WindowCleanEvent.WASHER -> TODO()

            WindowCleanEvent.SOLUTION -> {

            }
        }

        touchImage = BitmapFactory.decodeResource(resources, resourceId)

        invalidate()
    }
}