package `22week`

import java.util.LinkedList
import java.util.PriorityQueue
import java.util.Queue

lateinit var board: Array<List<Int>>

data class Position(val x: Int, val y: Int) {
    companion object {
        fun from(ints: List<Int>) = Position(ints[0] - 1, ints[1] - 1)
    }

    fun distanceTo(other: Position): Int {
        if (!isValidPosition(this) || !isValidPosition(other)) {
            throw IllegalStateException()
        }

        val directions = listOf(
            Position(0, 1), Position(1, 0), Position(0, -1), Position(-1, 0)
        )

        val visited = Array(board.size) { BooleanArray(board[0].size) }
        val queue: Queue<Pair<Position, Int>> = LinkedList()

        queue.add(Pair(this, 0))
        visited[this.x][this.y] = true

        while (queue.isNotEmpty()) {
            val (current, distance) = queue.poll()

            if (current == other) {
                return distance
            }

            for (direction in directions) {
                val next = Position(current.x + direction.x, current.y + direction.y)
                if (isValidPosition(next) && !visited[next.x][next.y]) {
                    visited[next.x][next.y] = true
                    queue.add(Pair(next, distance + 1))
                }
            }
        }

        throw IllegalStateException()
    }

    private fun isValidPosition(pos: Position): Boolean {
        return pos.x in board.indices && pos.y in board[0].indices && board[pos.x][pos.y] == 0
    }
}

lateinit var taxi: Position

data class Passenger(val index: Int, val start: Position, val end: Position) : Comparable<Passenger> {
    override fun compareTo(other: Passenger) = start.distanceTo(taxi).compareTo(other.start.distanceTo(taxi)).let {
        if (it == 0) {
            index - other.index
        } else it
    }
}

fun main() {
    var (N, M, F) = readln().split(" ").map { it.toInt() }
    board = Array(N) { readln().split(" ").map { it.toInt() } }
    taxi = Position.from(readln().split(" ").map { it.toInt() })
    val passengers = List(M) {
        val (start, end) = readln().split(" ").map { it.toInt() }.chunked(2).map {
            Position.from(it)
        }
        Passenger(it, start, end)
    }
    try {
        val passengerQueue = PriorityQueue(passengers)
        while (passengerQueue.isNotEmpty()) {
            val (_, start, end) = passengerQueue.peek()
            val distance = taxi.distanceTo(start)
            if (distance > F) {
                println(-1)
                return
            }
            F -= distance
            taxi = start
            val endDistance = taxi.distanceTo(end)
            if (endDistance > F) {
                println(-1)
                return
            }
            taxi = end
            F -= endDistance
            F += endDistance * 2
            passengerQueue.poll()
        }
        println(F)
    } catch (e: IllegalStateException) {
        println(-1)
    }
}
