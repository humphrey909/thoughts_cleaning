package com.example.thoughts_cleaning.util

import android.graphics.RectF
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

class GameState(width: Int, height: Int) {
    // 💡 여러 스레드에서 접근할 수 있으므로 CopyOnWriteArrayList를 사용하여 안전하게 관리합니다.
    val items = CopyOnWriteArrayList<Item>()

    // 플레이어 객체 (실제 구현에 맞게 Player 클래스를 가정합니다.)
    val player = Player(
        x = (width / 2).toFloat(),
        y = (height / 2).toFloat(),
        size = 50f
    )

    // 아이템을 랜덤한 위치에 생성하는 함수
    fun spawnItem(screenWidth: Int, screenHeight: Int) {
        // 화면 경계를 벗어나지 않도록 랜덤 위치 설정
        val margin = 100

        val randomX = (margin..screenWidth - margin).random().toFloat()
        val randomY = (margin..screenHeight - margin).random().toFloat()

        val newItem = Item(
            x = randomX,
            y = randomY,
            radius = 30f,
            type = if ((0..100).random() < 30) ItemType.SPEED_BOOST else ItemType.DEFAULT
        )
        items.add(newItem)
    }
}

// 플레이어 클래스 (예시)
data class Player(
    var x: Float,
    var y: Float,
    val size: Float,
    var health: Int = 100
) {
    fun getBounds(): RectF {
        return RectF(
            x - size / 2,
            y - size / 2,
            x + size / 2,
            y + size / 2
        )
    }
}