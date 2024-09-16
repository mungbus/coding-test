package `13week`

import java.util.PriorityQueue
import kotlin.Int.Companion.MAX_VALUE

private fun dijkstra(start: Int, n: Int, graph: Array<MutableList<Pair<Int, Int>>>): IntArray {
    val dist = IntArray(n + 1) { MAX_VALUE }
    dist[start] = 0
    val pq = PriorityQueue(compareBy<Pair<Int, Int>> { it.second })
    pq.add(start to 0)

    // (노드, 거리) 계산
    while (pq.isNotEmpty()) {
        val (currentNode, currentDist) = pq.poll()
        if (currentDist > dist[currentNode]) continue
        for (edge in graph[currentNode]) {
            val (nextNode, weight) = edge
            val distance = currentDist + weight
            if (distance < dist[nextNode]) {
                dist[nextNode] = distance
                pq.add(nextNode to distance)
            }
        }
    }

    return dist
}

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val J = readln().toInt()
    readln()
    val housesA = readln().split(" ").map { it.toInt() }
    val housesB = readln().split(" ").map { it.toInt() }

    val graph = Array(N + 1) { mutableListOf<Pair<Int, Int>>() }
    repeat(M) {
        val (X, Y, Z) = readln().split(" ").map { it.toInt() }
        graph[X].add(Pair(Y, Z))
        graph[Y].add(Pair(X, Z))
    }

    // 진서(J)의 집에서 각 집까지의 최단 거리 계산
    val distFromJ = dijkstra(J, N, graph)

    // A형 집들 중 가장 짧은 거리
    val minADist = housesA.minOf { distFromJ[it] }

    // B형 집들 중 가장 짧은 거리
    val minBDist = housesB.minOf { distFromJ[it] }

    // 결과 출력
    when {
        minADist == MAX_VALUE && minBDist == MAX_VALUE -> {
            println("-1")
        }

        minADist <= minBDist -> {
            println("A")
            println(minADist)
        }

        else -> {
            println("B")
            println(minBDist)
        }
    }
}
