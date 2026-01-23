package `260124`

import java.util.LinkedList
import java.util.Queue

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

fun main() {
    val K = readln().toInt()
    val (W, H) = readln().split(" ").map { it.toInt() }
    val map = Array(H) {
        readln().split(" ").map { it.toInt() }
    }

    // 일반 이동 (상하좌우)
    val normalDir = arrayOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )

    // 말의 이동 (나이트 이동)
    val horseDir = arrayOf(
        -2 to -1,
        -2 to 1,
        -1 to -2,
        -1 to 2,
        1 to -2,
        1 to 2,
        2 to -1,
        2 to 1
    )

    // visited[y][x][k] = (y, x)에 말 이동을 k번 사용하여 도착했는지
    val visited = Array(H) { Array(W) { BooleanArray(K + 1) } }

    val queue: Queue<Pair<Pair<Int, Int>, Int>> = LinkedList() // ((y, x), 남은 말 이동 횟수)
    val dist = Array(H) { Array(W) { IntArray(K + 1) { -1 } } }

    queue.offer((0 to 0) to K)
    visited[0][0][K] = true
    dist[0][0][K] = 0

    var answer = -1

    while (queue.isNotEmpty()) {
        val (pos, horseMoves) = queue.poll()
        val (y, x) = pos

        // 도착지점에 도달
        if (y == H - 1 && x == W - 1) {
            if (answer == -1 || dist[y][x][horseMoves] < answer) {
                answer = dist[y][x][horseMoves]
            }
            continue
        }

        // 일반 이동 (상하좌우)
        for (d in normalDir) {
            val (ny, nx) = pos + d

            if (ny in 0..<H && nx in 0..<W &&
                map[ny][nx] == 0 && !visited[ny][nx][horseMoves]
            ) {
                visited[ny][nx][horseMoves] = true
                dist[ny][nx][horseMoves] = dist[y][x][horseMoves] + 1
                queue.offer((ny to nx) to horseMoves)
            }
        }

        // 말의 이동 (남은 횟수가 있을 때만)
        if (horseMoves > 0) {
            for (d in horseDir) {
                val (ny, nx) = pos + d

                if (ny in 0..<H && nx in 0..<W &&
                    map[ny][nx] == 0 && !visited[ny][nx][horseMoves - 1]
                ) {
                    visited[ny][nx][horseMoves - 1] = true
                    dist[ny][nx][horseMoves - 1] = dist[y][x][horseMoves] + 1
                    queue.offer((ny to nx) to (horseMoves - 1))
                }
            }
        }
    }

    println(answer)
}
