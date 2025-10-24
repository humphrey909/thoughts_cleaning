package com.example.thoughts_cleaning.util

class JoystickState {
    // volatile 키워드로 스레드 간 동기화 보장 (여기서는 간단히 Float 사용)
    @Volatile var angle: Float = 0f
    @Volatile var strength: Float = 0f

    fun update(newAngle: Float, newStrength: Float) {
        this.angle = newAngle
        this.strength = newStrength
    }
}