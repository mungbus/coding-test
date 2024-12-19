package `19week`

import java.util.LinkedList
import java.util.Queue

fun main() {
    val T = 100
    val (N, M) = readln().split(" ").map { it.toInt() }
    val board = Array(N + M) {
        val (x, y) = readln().split(" ").map { it.toInt() }
        x to y
    }.toMap()

    val visited = BooleanArray(T)
    val queue: Queue<Pair<Int, Int>> = LinkedList()

    queue.add(Pair(1, 0))
    visited[0] = true


    while (queue.isNotEmpty()) {
        val (current, cnt) = queue.poll()

        if (current == T) {
            println(cnt)
            return
        }

        for (dice in 1..6) {
            val next = current + dice
            if (next <= T && !visited[next - 1]) {
                visited[next - 1] = true
                val apply = board[next] ?: next
                queue.add(Pair(apply, cnt + 1))
            }
        }
    }
}
