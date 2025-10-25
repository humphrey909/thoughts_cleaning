package com.example.thoughts_cleaning.util

import android.graphics.RectF
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

class GameState(width: Int, height: Int) {
    // 💡 여러 스레드에서 접근할 수 있으므로 CopyOnWriteArrayList를 사용하여 안전하게 관리합니다.
//    val items = CopyOnWriteArrayList<Item>()
    val items = ArrayList<Item>()

    // 플레이어 객체 (실제 구현에 맞게 Player 클래스를 가정합니다.)
    val player = Player(
        x = (width / 2).toFloat(),
        y = (height / 2).toFloat(),
        radius = 50f
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
            radius = 50f,
            type = if ((0..100).random() < 30) ItemType.SPEED_BOOST else ItemType.DEFAULT
        )
        items.add(newItem)
    }
}

// 플레이어 클래스 (예시)
data class Player(
    var x: Float,
    var y: Float,
    val radius: Float,
    var health: Int = 100
) {
    fun getBounds(): RectF {
        return RectF(
            x - radius,
            y - radius,
            x + radius,
            y + radius
        )
    }
    /**
     * 캐릭터의 위치를 업데이트합니다.
     * @param dx X축으로 이동할 거리 (변화량)
     * @param dy Y축으로 이동할 거리 (변화량)
     */
    fun move(dx: Float, dy: Float) {
        this.x = dx
        this.y = dy
        // 여기에 움직임이 발생했을 때 필요한 추가 로직을 넣을 수 있습니다.
        // 예를 들어:
        // - 경계(맵) 체크 및 위치 조정
        // - 애니메이션 상태 업데이트
        // - 서버로 위치 정보 전송 등
    }
}