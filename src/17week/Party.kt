package `17week`

import java.util.PriorityQueue

fun dijkstra(N: Int, start: Int, way: Array<IntArray>): IntArray {
    val wayTime = IntArray(N) { Int.MAX_VALUE }
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
    wayTime[start] = 0
    pq.add(Pair(start, 0))

    while (pq.isNotEmpty()) {
        val (currentNode, currentTime) = pq.poll()
        if (currentTime > wayTime[currentNode]) continue
        repeat(N) {
            val appendTime = way[currentNode][it]
            if (appendTime != 0) {
                val newTime = currentTime + appendTime
                if (newTime < wayTime[it]) {
                    wayTime[it] = newTime
                    pq.add(Pair(it, newTime))
                }
            }
        }
    }

    return wayTime
}

fun main() {
    val (N, M, X) = readln().split(" ").map { it.toInt() }
    val way = Array(N) { IntArray(N) }
    val comeWay = Array(N) { IntArray(N) }

    repeat(M) {
        val (A, B, T) = readln().split(" ").map { it.toInt() }
        way[A - 1][B - 1] = T
        comeWay[B - 1][A - 1] = T
    }

    val toX = dijkstra(N, X - 1, comeWay)
    val fromX = dijkstra(N, X - 1, way)

    println((0..<N).maxOf {
        toX[it] + fromX[it]
    })
}
