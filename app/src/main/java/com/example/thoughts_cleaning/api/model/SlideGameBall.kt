package com.example.thoughts_cleaning.api.model

data class SlideGameBall (
    var x: Float,
    var y: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var isGoal: Boolean = false,
    val color: Int,
    val text: String,

    // [추가] 원래 위치 기억 & 복귀 모드 플래그
    var startX: Float = 0f,
    var startY: Float = 0f,
    var isReturning: Boolean = false,
    var goalTime: Long = 0L
)