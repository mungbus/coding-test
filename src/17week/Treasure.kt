package `17week`

import java.util.LinkedList
import java.util.Queue

private val directions = listOf(
    Pair(-1, 0),
    Pair(1, 0),
    Pair(0, -1),
    Pair(0, 1)
)

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + other.first, second + other.second)
}

fun Array<BooleanArray>.fromPos(pos: Pair<Int, Int>): Boolean {
    return pos.first in indices && pos.second in this[0].indices && this[pos.first][pos.second]
}

fun Array<BooleanArray>.longestPath(): Int {
    val row = size
    if (row <= 0) return 0
    val col = this[0].size

    fun Pair<Int, Int>.bfs(): Int {
        val queue: Queue<Pair<Pair<Int, Int>, Int>> = LinkedList()
        val visited = Array(row) { BooleanArray(col) { false } }
        queue.add(Pair(this, 0))
        visited[first][second] = true
        var longest = 0

        while (queue.isNotEmpty()) {
            val (current, dist) = queue.poll()
            longest = dist

            for (dir in directions) {
                val next = current + dir
                if (fromPos(next) && !visited[next.first][next.second]) {
                    visited[next.first][next.second] = true
                    queue.add(Pair(next, dist + 1))
                }
            }
        }
        return longest
    }

    return (0..<row).maxOf { i ->
        (0..<col).mapNotNull { j ->
            if (this[i][j]) Pair(i, j).bfs() else null
        }.maxOrNull() ?: 0
    }
}

fun main() {
    val (R) = readln().split(" ").map { it.toInt() }
    val map = Array(R) {
        readln().split("").map {
            it.equals("L")
        }.toBooleanArray()
    }

    println(map.longestPath())
}
