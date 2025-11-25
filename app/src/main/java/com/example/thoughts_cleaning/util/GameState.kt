package com.example.thoughts_cleaning.util

import android.graphics.RectF
import android.util.Log
import com.example.thoughts_cleaning.api.model.GameWall
import kotlin.random.Random

class GameState(width: Int, height: Int) {
    // 💡 여러 스레드에서 접근할 수 있으므로 CopyOnWriteArrayList를 사용하여 안전하게 관리합니다.
//    val items = CopyOnWriteArrayList<Item>()
    val items = ArrayList<Item>()

    val walls: MutableList<GameWall>? = mutableListOf() //사물이 있는 공간
    val wallsNot: MutableList<GameWall>? = mutableListOf() //사물이 없는 공간

    // 캐릭터 현재 위치
//     var charX: Float = 0f
//     var charY: Float = 0f

    // 플레이어 객체
    val player = Player(
        x = (width / 2).toFloat(),
        y = (height / 2).toFloat(),
        radius = 50f
    )

    var playerLastX = 0f
    var playerLastY = 0f

    var itemRadius = 50f


    // 아이템을 랜덤한 위치에 생성하는 함수
//    fun spawnItem(screenWidth: Int, screenHeight: Int) {
//        // 화면 경계를 벗어나지 않도록 랜덤 위치 설정
//        val margin = 100
//
//        val randomX = (margin..screenWidth - margin).random().toFloat()
//        val randomY = (margin..screenHeight - margin).random().toFloat()
//
//        val newItem = Item(
//            x = randomX,
//            y = randomY,
//            radius = 50f,
//            type = if ((0..100).random() < 30) ItemType.SPEED_BOOST else ItemType.DEFAULT
//        )
//        items.add(newItem)
//    }

    fun makeSpawnItems(itemTotalCount: Int) {
        // 화면 경계를 벗어나지 않도록 설정할 여백 (기존 코드와 동일하게 100 사용)
//        val margin = 80

        // items 리스트에 아이템을 추가하기 전에, 필요하다면 기존 아이템을 클리어할 수 있습니다.
        // items.clear() // (선택 사항: 기존 아이템을 제거하고 싶다면 주석 해제)

        var itemCount = 0
        // 1. buildList를 사용하여 아이템을 itemCount만큼 한 번에 생성합니다.
        val newItemsList = buildList {
            repeat(itemTotalCount) {
//                Log.d("canvas", "itemCount: ${itemCount}") // 5

                var randomX = 0f
                var randomY = 0f
                var wall: GameWall

                if(walls!!.size > itemCount){
                    wall = walls.get(itemCount)
                }else{
                    wall = wallsNot!!.get(itemCount-walls.size)
                }

                //반지름을 적용하여 좌표를 재 조정한다.
                // 1. X축 허용 범위 계산: 벽의 좌우 경계를 반지름만큼 안쪽으로 축소
                val minSafeX = wall.left + itemRadius
                val maxSafeX = wall.right - itemRadius

                // 2. Y축 허용 범위 계산: 벽의 상하 경계를 반지름만큼 안쪽으로 축소
                val minSafeY = wall.top + itemRadius
                val maxSafeY = wall.bottom - itemRadius

                // 3. X 좌표 랜덤 생성: minSafeX와 maxSafeX 사이에서 랜덤 선택
                // 공식: min + (range) * randomFloat
                val rangeX = maxSafeX - minSafeX
                randomX = minSafeX + rangeX * Random.nextFloat()

                // 4. Y 좌표 랜덤 생성: minSafeY와 maxSafeY 사이에서 랜덤 선택
                val rangeY = maxSafeY - minSafeY
                randomY = minSafeY + rangeY * Random.nextFloat()

                val type = ItemType.DEFAULT

                // Item 객체를 생성하여 buildList의 내부 리스트에 추가합니다.
                add(Item(
                    x = randomX,
                    y = randomY,
                    radius = itemRadius,
                    type = type
                ))

                itemCount++
            }
        }

        // 2. 생성된 전체 리스트(newItemsList)를 기존 items 리스트에 한 번의 호출로 추가합니다.
        items.addAll(newItemsList)
    }

    enum class GameStateFlow {COMMON, ZOOMING, CLEANING_MODE}
}

// 플레이어 클래스 (예시)
data class Player(
    var x: Float,
    var y: Float,
    val radius: Float,
    var health: Int = 100
) {
//    var lastX: Float = 0f
//    var lastY: Float = 0f

//    fun Player(x: Float, y: Float, size: Float, color: Int) {
//        rect = RectF(x, y, x + size, y + size)
//        paint = Paint()
//        paint.setColor(color)
//        lastX = x
//        lastY = y
//    }

    fun setBounds(x: Float, y: Float){
        this.x = x
        this.y = y
    }

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



//class Wall(left: Float, top: Float, right: Float, bottom: Float, color: Int) {
//    var rect: RectF
//    var paint: Paint
//
//    init {
//        rect = RectF(left, top, right, bottom)
//        paint = Paint()
//        paint.setColor(color)
//    }
//
//    fun draw(canvas: Canvas) {
//        canvas.drawRect(rect, paint)
//    }
//}





