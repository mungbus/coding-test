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

    val waterTime = Array(R) {
        IntArray(C) {
            Int.MAX_VALUE
        }
    }

    var startHedgehog: Point? = null

    repeat(R) { r ->
        repeat(C) { c ->
            when (board[r][c]) {
                'S' -> startHedgehog = Point(r to c, 0)
                '*' -> {
                    waterQueue.add(Point(r to c, 0))
                    waterTime[r][c] = 0 // 물 시작점 시간 설정
                }
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
    while (waterQueue.isNotEmpty()) {
        val (pos, time) = waterQueue.removeFirst()

        directions.forEach { dir ->
            val next = pos + dir
            val (nr, nc) = next

            if (isValid(next) && !setOf('X', 'D').contains(board[nr][nc]) && waterTime[nr][nc] == Int.MAX_VALUE) {
                val nextTime = time + 1
                waterTime[nr][nc] = nextTime
                waterQueue.add(Point(next, nextTime))
            }
        }
    }

    // 고슴도치 이동, 물이 차기 전에 도착 가능한지 확인
    startHedgehog?.let { hedgehog ->
        hedgehogQueue.add(hedgehog)
        val visited = Array(R) { BooleanArray(C) }
        visited[hedgehog.pos.first][hedgehog.pos.second] = true

        while (hedgehogQueue.isNotEmpty()) {
            val (pos, time) = hedgehogQueue.removeFirst()

            directions.forEach { dir ->
                val next = pos + dir
                val (nr, nc) = next

                if (isValid(next) && !visited[nr][nc]) {
                    val nextHedgehogTime = time + 1

                    // 비버굴 도착
                    if (board[nr][nc] == 'D') {
                        println(nextHedgehogTime)
                        return
                    }

                    // 물이 차는 시간 확인
                    if (board[nr][nc] == '.' && nextHedgehogTime < waterTime[nr][nc]) {
                        visited[nr][nc] = true
                        hedgehogQueue.add(Point(next, nextHedgehogTime))
                    }
                }
            }
        }
    }

    println("KAKTUS")
}
