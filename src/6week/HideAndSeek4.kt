package `6week`

import java.util.LinkedList
import java.util.Queue

class HideAndSeek4(private val K: Int) {
    private val MAX = 100000
    private val moveList = listOf<(Int) -> Int>({ it + 1 }, { it - 1 }, { it * 2 })
    private val visited = BooleanArray(MAX + 1)
    private val path = IntArray(MAX + 1)
    fun seek(N: Int): List<Int> {
        val queue: Queue<Int> = LinkedList()
        queue.offer(N)
        visited[N] = true
        while (queue.isNotEmpty()) {
            val N = queue.poll()
            if (N == K) break
            moveList.forEach {
                val next = it(N)
                if (next >= 0 && next <= MAX && !visited[next]) {
                    visited[next] = true
                    path[next] = N
                    queue.add(next)
                }
            }
        }
        return findPath(N)
    }

    fun findPath(N: Int): List<Int> {
        return LinkedList<Int>().apply {
            var before = K
            while (before != N) {
                before = path[before]
                addFirst(before)
            }
        }
    }
}

fun main() {
    val (N, K) = readln().split(" ").map { it.toInt() }
    if (N == K) println("0\n$K")
    else HideAndSeek4(K).seek(N).let {
        println("${it.size}\n${it.joinToString(" ")} $K")
    }
}
