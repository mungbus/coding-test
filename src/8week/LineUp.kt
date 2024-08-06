package `8week`

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.LinkedList
import java.util.Queue

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val (N, M) = br.readLine().split(" ").map { it.toInt() }

    val indegree = IntArray(N + 1)
    val graph: List<MutableList<Int>> = List(N + 1) {
        mutableListOf()
    }
    repeat(M) {
        val (a, b) = br.readLine().split(" ").map { it.toInt() }
        graph[a].add(b)
        indegree[b]++
    }
    val q: Queue<Int> = LinkedList()
    for (i in 1..<indegree.size) {
        if (indegree[i] == 0) {
            q.offer(i)
        }
    }

    val result: Queue<Int> = LinkedList()
    var current: Int
    while (!q.isEmpty()) {
        current = q.poll()
        result.offer(current)
        for (i in graph[current]) {
            indegree[i]--
            if (indegree[i] == 0) {
                q.offer(i)
            }
        }
    }
    println(result.joinToString(" "))
}
