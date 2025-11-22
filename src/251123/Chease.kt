package `251123`

import java.util.ArrayDeque

// 좌표 계산을 간결하게 만들기 위한 Pair 덧셈 연산자
private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    (first + other.first) to (second + other.second)

fun main() {
    // 보드 크기와 초기 상태 입력
    val (N, M) = readln().trim().split(" ").map { it.toInt() }
    val cheaseMap = Array(N) { IntArray(M) }
    repeat(N) { row ->
        val rowValues = readln().trim().split(" ").map { it.toInt() }
        for (col in 0 until M) {
            cheaseMap[row][col] = rowValues[col]
        }
    }

    // 치즈가 모두 녹을 때까지 턴을 반복
    var turns = 0
    while (true) {
        val melted = meltCheese(cheaseMap)
        if (melted == 0) break
        turns++
    }
    println(turns)
}

private fun meltCheese(cheaseMap: Array<IntArray>): Int {
    val rows = cheaseMap.size
    val cols = cheaseMap[0].size
    val visited = Array(rows) { BooleanArray(cols) } // 외부 공기 방문 여부
    val exposures = Array(rows) { IntArray(cols) }   // 각 치즈 면의 외부 공기 노출 횟수
    val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
    val directions = arrayOf(
        1 to 0,
        -1 to 0,
        0 to 1,
        0 to -1
    )

    // (0,0)에서 시작해 외부 공기를 BFS로 표시
    queue.add(0 to 0)
    visited[0][0] = true

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        for (dir in directions) {
            val (nx, ny) = current + dir
            if (nx !in 0 until rows || ny !in 0 until cols) continue
            if (cheaseMap[nx][ny] == 0 && !visited[nx][ny]) {
                // 외부 공기 확장
                visited[nx][ny] = true
                queue.add(nx to ny)
            } else if (cheaseMap[nx][ny] == 1) {
                // 외부 공기와 맞닿은 치즈 면 카운트
                exposures[nx][ny]++
            }
        }
    }

    // 두 면 이상 외부 공기에 노출된 치즈만 녹인다
    var melted = 0
    repeat(rows) { i ->
        repeat(cols) { j ->
            if (cheaseMap[i][j] == 1 && exposures[i][j] >= 2) {
                cheaseMap[i][j] = 0
                melted++
            }
        }
    }
    return melted
}
