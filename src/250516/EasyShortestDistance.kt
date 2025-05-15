package `250516`

import java.util.LinkedList
import java.util.Queue

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
}

private val directions = listOf(
    Position(0, 1),
    Position(1, 0),
    Position(0, -1),
    Position(-1, 0)
)

fun main() {
    val (n, m) = readln().split(" ").map { it.toInt() }
    val distances = Array(n) { IntArray(m) { -1 } }
    var startPos: Position? = null
    val map: List<List<Int?>> = Array(n) { j ->
        readln().split(" ").mapIndexed { i, v ->
            when (val value: Int = v.toInt()) {
                2 -> {
                    startPos = Position(i, j)
                    0
                }

                0 -> {
                    distances[j][i] = 0
                    0
                }

                else -> value
            }
        }
    }.toList()

    startPos?.let { start ->
        val queue: Queue<Position> = LinkedList()

        distances[start.y][start.x] = 0
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            directions.forEach { dir ->
                val next = current + dir
                if (next.x in 0..<m && next.y in 0..<n && map[next.y][next.x] == 1 && distances[next.y][next.x] == -1) {
                    distances[next.y][next.x] = distances[current.y][current.x] + 1
                    queue.add(next)
                }
            }
        }

        println(distances.joinToString("\n") { it.joinToString(" ") })
    }
}
