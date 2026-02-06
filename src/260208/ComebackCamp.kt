package `260208`

import java.util.LinkedList
import java.util.Queue

class Solution {
    fun solution(n: Int, roads: Array<IntArray>, sources: IntArray, destination: Int): IntArray {
        // 1. 인접 리스트로 그래프 초기화
        val graph = Array(n + 1) { mutableListOf<Int>() }
        for (road in roads) {
            graph[road[0]].add(road[1])
            graph[road[1]].add(road[0])
        }

        // 2. 최단 거리를 저장할 배열 (초기값 -1)
        val distances = IntArray(n + 1) { -1 }

        // 3. BFS 수행 (부대 위치 destination에서 시작)
        bfs(destination, graph, distances)

        // 4. sources 순서대로 결과 배열 생성
        val answer = IntArray(sources.size)
        for (i in sources.indices) {
            answer[i] = distances[sources[i]]
        }

        return answer
    }

    private fun bfs(start: Int, graph: Array<MutableList<Int>>, distances: IntArray) {
        val queue: Queue<Int> = LinkedList()

        queue.add(start)
        distances[start] = 0 // 시작 지점(부대)의 거리는 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            for (neighbor in graph[current]) {
                // 아직 방문하지 않은 지역인 경우에만 탐색
                if (distances[neighbor] == -1) {
                    distances[neighbor] = distances[current] + 1
                    queue.add(neighbor)
                }
            }
        }
    }
}

fun main() {
    val solution = Solution()
    val n = 5
    val roads = arrayOf(intArrayOf(1, 2), intArrayOf(1, 4), intArrayOf(2, 4), intArrayOf(2, 5), intArrayOf(4, 5))
    val sources = intArrayOf(1, 3, 5)
    val destination = 5

    val result = solution.solution(n, roads, sources, destination)
    println(result.joinToString(", ")) // Expected output: 2, -1, 0
}
