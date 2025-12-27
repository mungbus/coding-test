package `251228`

import java.util.LinkedList
import java.util.Queue

val directions = arrayOf(
    Pair(-1, 0), // up
    Pair(1, 0),  // down
    Pair(0, -1), // left
    Pair(0, 1)   // right
)


fun main() {
    val (n, m) = readln().split(" ").map { it.toInt() }

    val grid = Array(n) { IntArray(m) }
    repeat(n) { i ->
        grid[i] = readln().split(" ").map { it.toInt() }.toIntArray()
    }

    var year = 0

    while (true) {
        // 현재 빙산 덩어리 개수 카운트
        val visited = Array(n) { BooleanArray(m) }
        var componentCount = 0

        repeat(n) { i ->
            repeat(m) { j ->
                if (grid[i][j] > 0 && !visited[i][j]) {
                    bfs(i, j, grid, visited, n, m)
                    componentCount++
                }
            }
        }

        // 빙산이 2개 이상으로 분리되었는지 확인
        if (componentCount >= 2) {
            println(year)
            return
        }

        // 빙산이 완전히 녹았는지 확인
        if (componentCount == 0) {
            println(0)
            return
        }

        // 한 해가 지나감 - 높이 감소
        val newGrid = grid.map { it.copyOf() }.toTypedArray()

        repeat(n) { i ->
            repeat(m) { j ->
                if (grid[i][j] > 0) {
                    val seaAdjacentCount = directions.count { (di, dj) ->
                        val ni = i + di
                        val nj = j + dj
                        ni in 0..<n && nj >= 0 && nj < m && grid[ni][nj] == 0
                    }
                    newGrid[i][j] = maxOf(0, grid[i][j] - seaAdjacentCount)
                }
            }
        }

        repeat(n) { i ->
            grid[i] = newGrid[i]
        }

        year++
    }
}

fun bfs(startI: Int, startJ: Int, grid: Array<IntArray>, visited: Array<BooleanArray>, n: Int, m: Int) {
    val queue: Queue<Pair<Int, Int>> = LinkedList()
    queue.add(Pair(startI, startJ))
    visited[startI][startJ] = true

    while (queue.isNotEmpty()) {
        val (i, j) = queue.poll()

        for ((di, dj) in directions) {
            val ni = i + di
            val nj = j + dj

            if (ni in 0..<n && nj >= 0 && nj < m && !visited[ni][nj] && grid[ni][nj] > 0) {
                visited[ni][nj] = true
                queue.add(Pair(ni, nj))
            }
        }
    }
}
