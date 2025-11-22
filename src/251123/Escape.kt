package `251123`

import java.util.ArrayDeque

data class Point(val pos: Pair<Int, Int>, val time: Int = 0)

fun main() {
    val (R, C) = readln().trim().split(" ").map { it.toInt() }
    val waterQueue = ArrayDeque<Point>()
    val hedgehogQueue = ArrayDeque<Point>()

    // 지도 입력 및 큐 초기화
    val board = Array(R) { r ->
        readln().trim().toCharArray().apply {
            forEachIndexed { c, char ->
                when (char) {
                    '*' -> waterQueue.add(Point(r to c))
                    'S' -> hedgehogQueue.add(Point(r to c, 0))
                }
            }
        }
    }

    val directions = listOf(
        -1 to 0, 1 to 0, 0 to -1, 0 to 1
    )

    fun isValid(pos: Pair<Int, Int>) = pos.first in 0 until R && pos.second in 0 until C

    // BFS
    while (hedgehogQueue.isNotEmpty()) {

        // 물 확산
        repeat(waterQueue.size) {
            val (wr, wc) = waterQueue.removeFirst().pos // 현재 물 위치

            directions.map { (dr, dc) ->
                (wr + dr) to (wc + dc)
            }.filter { pos ->
                // 범위 내 && 빈 곳('.')이거나 고슴도치('S')인 경우 확산 가능
                isValid(pos) && (board[pos.first][pos.second] == '.' || board[pos.first][pos.second] == 'S')
            }.forEach { pos ->
                board[pos.first][pos.second] = '*'
                waterQueue.add(Point(pos))
            }
        }

        // 고슴도치 이동
        repeat(hedgehogQueue.size) {
            val hedgehog = hedgehogQueue.removeFirst()
            val (hr, hc) = hedgehog.pos

            // 현재 고슴도치 위치가 물에 잠겼다면, 이동 시도 X
            if (board[hr][hc] == '*') return@repeat

            directions.map { (dr, dc) ->
                (hr + dr) to (hc + dc)
            }.filter { pos ->
                isValid(pos)
            }.forEach { pos ->
                val cell = board[pos.first][pos.second]
                when (cell) {
                    'D' -> {
                        println(hedgehog.time + 1)
                        return
                    }

                    '.' -> {
                        board[pos.first][pos.second] = 'S' // 방문 처리
                        hedgehogQueue.add(Point(pos, hedgehog.time + 1))
                    }
                }
            }
        }
    }

    println("KAKTUS")
}
