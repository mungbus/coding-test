package `250920`

data class Position(val x: Int, val y: Int, val direction: Int)

class RobotCleaner {
    private val directions = listOf(
        Pair(-1, 0), // 북
        Pair(0, 1),  // 동
        Pair(1, 0),  // 남
        Pair(0, -1)  // 서
    )

    fun cleanRoom(room: Array<IntArray>, startX: Int, startY: Int, startDirection: Int): Int {
        val n = room.size
        val m = room[0].size
        var cleanedCount = 0
        var position = Position(startX, startY, startDirection)

        while (true) {
            val (x, y, direction) = position

            // 1. 현재 칸 청소
            if (room[x][y] == 0) {
                room[x][y] = 2 // 청소 완료 표시
                cleanedCount++
            }

            // 2. 주변 4칸 확인
            var found = false
            for (i in 0..<4) {
                val newDirection = (direction + 3 - i) % 4 // 반시계 방향으로 회전
                val nx = x + directions[newDirection].first
                val ny = y + directions[newDirection].second

                if (nx in 0..<n && ny in 0..<m && room[nx][ny] == 0) {
                    position = Position(nx, ny, newDirection)
                    found = true
                    break
                }
            }

            if (!found) {
                // 3. 후진
                val backDirection = (direction + 2) % 4
                val bx = x + directions[backDirection].first
                val by = y + directions[backDirection].second

                if (bx in 0..<n && by in 0..<m && room[bx][by] != 1) {
                    position = Position(bx, by, direction)
                } else {
                    // 4. 후진 불가 -> 작동 멈춤
                    break
                }
            }
        }

        return cleanedCount
    }
}

fun main() {
    val (n) = readln().split(" ").map { it.toInt() }
    val (startX, startY, startDirection) = readln().split(" ").map { it.toInt() }
    val room = Array(n) {
        readln().split(" ").map { it.toInt() }.toIntArray()
    }

    val robotCleaner = RobotCleaner()
    val result = robotCleaner.cleanRoom(room, startX, startY, startDirection)
    println(result)
}
