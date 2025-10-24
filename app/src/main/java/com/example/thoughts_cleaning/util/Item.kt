package com.example.thoughts_cleaning.util

import android.graphics.RectF

data class Item(
    var x: Float, // 아이템의 중앙 X 좌표
    var y: Float, // 아이템의 중앙 Y 좌표
    val radius: Float, // 아이템의 반지름
    val type: ItemType = ItemType.DEFAULT // 아이템의 종류
) {
    // 충돌 감지를 위한 경계(Bounding Box) 사각형을 반환합니다.
    fun getBounds(): RectF {
        return RectF(
            x - radius,
            y - radius,
            x + radius,
            y + radius
        )
    }
}

// 아이템 종류를 정의하는 enum
enum class ItemType {
    DEFAULT, SPEED_BOOST, HEALTH_PACK
}