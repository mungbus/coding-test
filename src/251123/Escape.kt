package `251123`

import java.util.ArrayDeque

data class Point(val pos: Pair<Int, Int>, val time: Int = 0)

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) =
    (first + other.first) to (second + other.second)

fun main() {
    val (R, C) = readln().trim().split(" ").map { it.toInt() }
    val board = Array(R) { readln().trim().toCharArray() }

    val waterQueue = ArrayDeque<Point>()
    val hedgehogQueue = ArrayDeque<Point>()

    // 물 도달 시간 기록 배열 (도달 불가능 시 MAX_VALUE)
    val waterTime = Array(R) { IntArray(C) { Int.MAX_VALUE } }

    var hedgehog: Point? = null
    var startWater: Point? = null

    // 초기화 및 시작점 찾기
    for (r in 0 until R) {
        for (c in 0 until C) {
            when (board[r][c]) {
                'S' -> hedgehog = Point(r to c, 0)
                '*' -> startWater = Point(r to c, 0)
            }
        }
    }

    val directions = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )

    fun isValid(pos: Pair<Int, Int>) = pos.first in 0 until R && pos.second in 0 until C

    // 각 칸에 물이 닿는 최소 시간 기록
    if (startWater != null) {
        waterQueue.add(startWater)
        waterTime[startWater.pos.first][startWater.pos.second] = 0

        while (waterQueue.isNotEmpty()) {
            val (pos, time) = waterQueue.removeFirst()

            for (dir in directions) {
                val next = pos + dir
                val (nr, nc) = next

                if (isValid(next) && board[nr][nc] != 'X' && board[nr][nc] != 'D' && waterTime[nr][nc] == Int.MAX_VALUE) {
                    waterTime[nr][nc] = time + 1
                    waterQueue.add(Point(next, time + 1))
                }
            }
        }
    }

    // 고슴도치 이동, 물이 차기 전에 도착 가능한지 확인
    hedgehog?.let { hedgehog ->
        hedgehogQueue.add(hedgehog)
        val visited = Array(R) { BooleanArray(C) }
        visited[hedgehog.pos.first][hedgehog.pos.second] = true

        while (hedgehogQueue.isNotEmpty()) {
            val (pos, time) = hedgehogQueue.removeFirst()

            for (dir in directions) {
                val next = pos + dir
                val (nr, nc) = next

                if (isValid(next) && !visited[nr][nc]) {

                    // 비버굴 도착!
                    if (board[nr][nc] == 'D') {
                        println(time + 1)
                        return
                    }

                    // 물이 차는 시간 확인
                    if (board[nr][nc] == '.' && (time + 1) < waterTime[nr][nc]) {
                        visited[nr][nc] = true
                        hedgehogQueue.add(Point(next, time + 1))
                    }
                }
            }
        }
    }

    println("KAKTUS")
}
