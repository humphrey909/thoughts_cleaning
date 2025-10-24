package com.example.thoughts_cleaning.util

// 상수 및 데이터 클래스 정의
const val MOVE_FACTOR = 0.5f // 속도 조절 상수
const val SECTION_HEIGHT = 1000f // 맵 섹션 하나의 가상 높이 (예: 1000픽셀)

/** 게임 내 좌표를 나타내는 벡터 */
data class Vector2(var x: Float, var y: Float)

/** 생성된 아이템의 정보 (월드 좌표 기준) */
data class GameItem(val type: String, val position: Vector2)

//==============================================================

class GameEngine(private val screenWidth: Int, private val screenHeight: Int) {

    // 맵/배경 전체의 스크롤 오프셋 (모든 배경 및 아이템 렌더링에 사용됨)
    val mapOffset = Vector2(0f, 0f)

    // 현재 맵에 존재하는 아이템 목록 (월드 좌표 기준)
    private val activeItems = mutableListOf<GameItem>()

    // 현재까지 생성된 맵 섹션의 인덱스 (위로 갈수록 값이 감소함)
    private var spawnedSectionIndex = 0

    init {
        // 초기 맵 섹션 생성 (시작 위치)
        spawnNewSection(0)
    }

    /**
     * 게임 로직 업데이트 (조이스틱 입력 처리)
     * @param angle 조이스틱 각도 (0~360)
     * @param strength 조이스틱 강도 (0~100)
     */
    fun updateGame(angle: Int, strength: Int) {

        // 1. 조이스틱의 각도를 라디안으로 변환
        val radian = Math.toRadians(angle.toDouble())

        // 2. 캐릭터가 움직이고자 하는 이동 벡터 계산
        // (moveY의 -sin은 Y축 감소가 위쪽 방향임을 의미)
        val moveX = strength * MOVE_FACTOR * Math.cos(radian).toFloat()
        val moveY = strength * MOVE_FACTOR * -Math.sin(radian).toFloat()

        // 3. **맵 오프셋 이동 (핵심)**: 캐릭터가 가려는 방향의 반대로 맵 전체를 이동시킨다.
        // 캐릭터가 위로(moveY < 0) 가면, mapOffset.y는 증가하여 배경이 아래로 스크롤된다.
        mapOffset.x -= moveX
        mapOffset.y -= moveY

        // 4. 새로운 맵 섹션 연결 로직
        // mapOffset.y의 현재 값을 SECTION_HEIGHT로 나누어 현재 맵 인덱스를 계산
        // (Y축이 아래로 갈수록 값이 커지므로, 위로 스크롤 시 mapOffset.y가 음수로 깊숙이 들어감)
        // 새로운 섹션은 Y 오프셋이 SECTION_HEIGHT만큼 '음수 방향'으로 이동할 때 생성되어야 합니다.
        // mapOffset.y가 -1000, -2000...을 넘어설 때 새로운 섹션 인덱스가 됩니다.
        val targetSectionIndex = (mapOffset.y / SECTION_HEIGHT).toInt()

        // 캐릭터가 위로 올라가서(targetSectionIndex가 감소) 새로운 맵 구간에 도달했는지 확인
        if (targetSectionIndex < spawnedSectionIndex) {
            spawnNewSection(targetSectionIndex)
            spawnedSectionIndex = targetSectionIndex

            // TODO: 화면 밖으로 완전히 벗어난 아이템은 여기서 제거 (메모리 관리)
        }
    }

    /**
     * 새로운 맵 섹션을 생성하고 아이템을 배치하는 함수
     */
    private fun spawnNewSection(sectionIndex: Int) {
        // 새로운 섹션의 가상 세계 Y 좌표 (최상단) 계산
        val newSectionY = sectionIndex * SECTION_HEIGHT

        println("✅ 새로운 맵 섹션 생성: 인덱스 $sectionIndex (월드 Y: $newSectionY)")

        // **아이템 배치 로직**: 새로운 섹션 중앙에 아이템을 하나 배치합니다.
        val itemPosition = Vector2(
            x = screenWidth / 2f, // 화면 중앙 X
            y = newSectionY + SECTION_HEIGHT / 2f // 섹션 중앙 Y
        )

        val newItem = GameItem("💎 아이템 #${-sectionIndex}", itemPosition)
        activeItems.add(newItem)
        println("-> 아이템 '${newItem.type}' 생성 완료.")
    }

    /**
     * 화면에 맵과 아이템을 그리는 로직 (SurfaceView.Canvas 대신 콘솔 출력으로 대체)
     */
    fun drawGame() {
        println("\n=== 렌더링 (화면 뷰) ===")
        println("맵 오프셋 Y: ${String.format("%.1f", mapOffset.y)}")
        println("캐릭터는 화면 중앙 (${screenWidth / 2}, ${screenHeight / 2})에 고정되어 있습니다.")

        activeItems.forEach { item ->
            // 아이템의 월드 좌표에서 맵 오프셋을 뺀 값이 화면 좌표가 된다.
            val screenX = item.position.x + mapOffset.x
            val screenY = item.position.y + mapOffset.y

            // 아이템이 화면에 보이는 범위(ScreenHeight) 내에 있을 때만 출력
            if (screenY > 0 && screenY < screenHeight) {
                println("  [Visible] ${item.type} -> 화면 Y: ${String.format("%.1f", screenY)}")
            }
        }
        println("========================\n")
    }
}