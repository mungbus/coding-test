package `250823`

import java.util.PriorityQueue

fun findMinimumCost(N: Int, edges: List<Triple<Int, Int, Int>>): Int {
    val graph = Array(N + 1) { mutableListOf<Pair<Int, Int>>() }
    edges.forEach { (a, b, cost) ->
        graph[a].add(b to cost)
        graph[b].add(a to cost)
    }

    val distances = IntArray(N + 1) { Int.MAX_VALUE }
    distances[1] = 0
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
    pq.add(1 to 0)

    while (pq.isNotEmpty()) {
        val (current, currentCost) = pq.poll()
        if (currentCost > distances[current]) continue

        for ((neighbor, cost) in graph[current]) {
            val newCost = currentCost + cost
            if (newCost < distances[neighbor]) {
                distances[neighbor] = newCost
                pq.add(neighbor to newCost)
            }
        }
    }

    return if (distances[N] == Int.MAX_VALUE) -1 else distances[N]
}

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val edges = mutableListOf<Triple<Int, Int, Int>>()
    repeat(M) {
        val (a, b, cost) = readln().split(" ").map { it.toInt() }
        edges.add(Triple(a, b, cost))
    }

    println(findMinimumCost(N, edges))
}
