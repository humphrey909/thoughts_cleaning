package com.example.thoughts_cleaning.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.thoughts_cleaning.views.main.view.activity.container.MainActivity
import com.example.thoughts_cleaning.views.main.view.fragment.MainFragment

class GameView(context: Context, val activity: MainActivity, var fragment: MainFragment, private val joystickState: JoystickState) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var gameThread: GameThread

    init {
        holder.addCallback(this)
        // 화면이 포커스를 받을 수 있도록 설정
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Surface가 생성되면 스레드를 시작합니다.
        gameThread = GameThread(holder, context, activity, fragment, joystickState)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // 화면 크기가 변경될 때 처리 (필요시)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface가 파괴되기 전에 스레드를 안전하게 종료합니다.
        var retry = true
        gameThread.isRunning = false
        while (retry) {
            try {
                gameThread.join()
                retry = false
            } catch (e: InterruptedException) {
                // 스레드 종료 대기 중 인터럽트 발생 시 재시도
            }
        }
    }

    fun pause() {
        if (::gameThread.isInitialized) {
            gameThread.isRunning = false
        }
    }

    fun resume() {
        if (::gameThread.isInitialized) {
            gameThread.isRunning = true
            // 새로운 스레드를 시작하거나 재개 로직이 필요할 수 있습니다.
            // 여기서는 단순화하여 run()에서 while(isRunning)만 제어합니다.
        }
    }
}