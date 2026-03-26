package `260327`

import kotlin.math.max

fun main() {
    val (n, m) = readln().split(" ").map { it.toInt() }
    val board = Array(n) { readln().split(" ").map { it.toInt() }.toIntArray() }
    val visited = Array(n) { BooleanArray(m) }

    // 부메랑 4가지 모양의 날개 오프셋: {날개1(dr, dc), 날개2(dr, dc)}
    val shapes = arrayOf(
        arrayOf(0 to -1, 1 to 0),  // 1번: 왼쪽, 아래
        arrayOf(-1 to 0, 0 to -1), // 2번: 위, 왼쪽
        arrayOf(-1 to 0, 0 to 1),  // 3번: 위, 오른쪽
        arrayOf(0 to 1, 1 to 0)    // 4번: 오른쪽, 아래
    )

    var maxStrength = 0

    fun solve(idx: Int, currentSum: Int) {
        // 모든 칸을 다 확인한 경우 (Base Case)
        if (idx == n * m) {
            maxStrength = max(maxStrength, currentSum)
            return
        }

        val r = idx / m
        val c = idx % m

        // 1. 현재 칸을 중심으로 부메랑 만들기 시도
        if (!visited[r][c]) {
            for (shape in shapes) {
                val (r1, c1) = r + shape[0].first to c + shape[0].second
                val (r2, c2) = r + shape[1].first to c + shape[1].second

                // 범위 체크 및 방문 체크
                if (r1 in 0 until n && c1 in 0 until m && r2 in 0 until n && c2 in 0 until m) {
                    if (!visited[r1][c1] && !visited[r2][c2]) {
                        // Backtracking: 상태 변경
                        visited[r][c] = true
                        visited[r1][c1] = true
                        visited[r2][c2] = true

                        solve(idx + 1, currentSum + (board[r][c] * 2) + board[r1][c1] + board[r2][c2])

                        // Backtracking: 원복
                        visited[r][c] = false
                        visited[r1][c1] = false
                        visited[r2][c2] = false
                    }
                }
            }
        }

        // 2. 현재 칸을 건너뛰기 (중심으로 쓰지 않음)
        solve(idx + 1, currentSum)
    }

    solve(0, 0)
    println(maxStrength)
}
