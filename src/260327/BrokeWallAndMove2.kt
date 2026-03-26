package `260327`

import java.util.LinkedList
import java.util.Queue

// 1. Node 데이터 클래스: 좌표(rc)를 Pair로 관리
data class Node(val rc: Pair<Int, Int>, val broken: Int, val dist: Int)

// 2. Pair<Int, Int>에 대한 + 연산자 오버로딩
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

val directions = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, -1),
    Pair(0, 1)
)

fun main() {
    val (n, m, k) = readln().split(" ").map { it.toInt() }
    val board = Array(n) {
        val line = readln()
        IntArray(m) { col -> line[col] - '0' }
    }
    // visited[row][col][broken_count]
    val visited = Array(n) {
        Array(m) {
            BooleanArray(k + 1)
        }
    }

    val queue: Queue<Node> = LinkedList()

    // 시작점 설정
    queue.add(Node(Pair(0, 0), 0, 1))
    visited[0][0][0] = true

    var result = -1

    while (queue.isNotEmpty()) {
        val (currPos, broken, dist) = queue.poll()
        val (r, c) = currPos

        if (r == n - 1 && c == m - 1) {
            result = dist
            break
        }

        directions.forEach { dir ->
            val nextPos = currPos + dir
            val (nr, nc) = nextPos

            if (nr in 0 until n && nc in 0 until m) {
                if (board[nr][nc] == 0) {
                    if (!visited[nr][nc][broken]) {
                        visited[nr][nc][broken] = true
                        queue.add(Node(nextPos, broken, dist + 1))
                    }
                } else {
                    if (broken < k && !visited[nr][nc][broken + 1]) {
                        visited[nr][nc][broken + 1] = true
                        queue.add(Node(nextPos, broken + 1, dist + 1))
                    }
                }
            }
        }
    }

    println(result)
}
