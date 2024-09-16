package `13week`

import java.util.PriorityQueue

const val INF = Int.MAX_VALUE

data class Edge(val node: Int, val weight: Int)

fun dijkstra(start: Int, n: Int, graph: Array<MutableList<Edge>>): IntArray {
    val dist = IntArray(n + 1) { INF }
    dist[start] = 0
    val pq = PriorityQueue(compareBy<Pair<Int, Int>> { it.first }) // (거리, 노드)
    pq.add(0 to start)

    while (pq.isNotEmpty()) {
        val (currentDist, currentNode) = pq.poll()

        if (currentDist > dist[currentNode]) continue

        for (edge in graph[currentNode]) {
            val nextNode = edge.node
            val weight = edge.weight
            val distance = currentDist + weight

            if (distance < dist[nextNode]) {
                dist[nextNode] = distance
                pq.add(distance to nextNode)
            }
        }
    }

    return dist
}

fun main() {
    // 입력 받기
    val (N, M) = readln().split(" ").map { it.toInt() }
    val J = readln().toInt()
    readln()
    val housesA = readln().split(" ").map { it.toInt() }
    val housesB = readln().split(" ").map { it.toInt() }

    val graph = Array(N + 1) { mutableListOf<Edge>() }
    repeat(M) {
        val (X, Y, Z) = readln().split(" ").map { it.toInt() }
        graph[X].add(Edge(Y, Z))
        graph[Y].add(Edge(X, Z))
    }

    // 진서의 집에서 각 집까지의 최단 거리 계산
    val distFromJ = dijkstra(J, N, graph)

    // A형 집들 중 가장 짧은 거리
    val minADist = housesA.minOf { distFromJ[it] }

    // B형 집들 중 가장 짧은 거리
    val minBDist = housesB.minOf { distFromJ[it] }

    // 결과 출력
    when {
        minADist == INF && minBDist == INF -> {
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
