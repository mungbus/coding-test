package `260412`

import java.util.LinkedList
import java.util.Queue

data class Node(val r: Int, val c: Int, val dist: Int = 0)

val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)

fun main() {
    val n = readln().toInt()
    val map = Array(n) { readln().split(" ").map { it.toInt() }.toIntArray() }
    val islandId = Array(n) { IntArray(n) { 0 } }
    val visited = Array(n) { BooleanArray(n) }

    // 1. 섬 라벨링 (BFS로 각 섬에 번호 매기기)
    var count = 0
    for (i in 0 until n) {
        for (j in 0 until n) {
            if (map[i][j] == 1 && !visited[i][j]) {
                count++
                markIsland(i, j, count, n, map, islandId, visited)
            }
        }
    }

    // 2. 가장 짧은 다리 찾기
    var minBridge = Int.MAX_VALUE
    for (id in 1..count) {
        minBridge = minOf(minBridge, getShortestBridge(id, n, islandId))
    }

    println(minBridge)
}

// 섬을 구분하기 위한 BFS
fun markIsland(
    r: Int,
    c: Int,
    id: Int,
    n: Int,
    map: Array<IntArray>,
    islandId: Array<IntArray>,
    visited: Array<BooleanArray>
) {
    val queue: Queue<Pair<Int, Int>> = LinkedList()
    queue.add(r to c)
    visited[r][c] = true
    islandId[r][c] = id

    while (queue.isNotEmpty()) {
        val (currR, currC) = queue.poll()
        directions.forEach {
            val (nr, nc) = (currR to currC) + it

            if (nr in 0 until n && nc in 0 until n && !visited[nr][nc] && map[nr][nc] == 1) {
                visited[nr][nc] = true
                islandId[nr][nc] = id
                queue.add(nr to nc)
            }
        }
    }
}

// 특정 섬(id)에서 출발하여 다른 섬에 도달하는 최단 거리 BFS
fun getShortestBridge(id: Int, n: Int, islandId: Array<IntArray>): Int {
    val queue: Queue<Node> = LinkedList()
    val distVisited = Array(n) { BooleanArray(n) }

    // 해당 섬의 모든 좌표를 초기 큐에 삽입
    for (i in 0 until n) {
        for (j in 0 until n) {
            if (islandId[i][j] == id) {
                distVisited[i][j] = true
                queue.add(Node(i, j, 0))
            }
        }
    }

    while (queue.isNotEmpty()) {
        val (currR, currC, d) = queue.poll()

        directions.forEach {
            val (nr, nc) = (currR to currC) + it


            if (nr in 0 until n && nc in 0 until n && !distVisited[nr][nc]) {
                if (islandId[nr][nc] == 0) { // 바다인 경우
                    distVisited[nr][nc] = true
                    queue.add(Node(nr, nc, d + 1))
                } else if (islandId[nr][nc] != id) { // 다른 섬을 만난 경우
                    return d
                }
            }
        }
    }
    return Int.MAX_VALUE
}
