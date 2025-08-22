package `250823`

import kotlin.math.abs

data class Position(val row: Int, val col: Int) {
    fun distanceTo(other: Position): Int = abs(row - other.row) + abs(col - other.col)
}

class ChickenDelivery(N: Int, map: List<List<Int>>) {
    private val houses = mutableListOf<Position>()
    private val chickens = mutableListOf<Position>()

    init {
        repeat(N) { i ->
            repeat(N) { j ->
                when (map[i][j]) {
                    1 -> houses.add(Position(i, j))
                    2 -> chickens.add(Position(i, j))
                }
            }
        }
    }

    fun solve(M: Int): Int {
        return combinations(chickens.indices.toList(), M)
            .minOf { selectedChickens ->
                houses.sumOf { house ->
                    selectedChickens.minOf { chickenIdx ->
                        house.distanceTo(chickens[chickenIdx])
                    }
                }
            }
    }

    private fun <T> combinations(items: List<T>, k: Int): Sequence<List<T>> = sequence {
        if (k == 0) {
            yield(emptyList())
            return@sequence
        }
        for (i in items.indices) {
            val item = items[i]
            combinations(items.drop(i + 1), k - 1).forEach { rest ->
                yield(listOf(item) + rest)
            }
        }
    }
}

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val map = List(N) { readln().split(" ").map { it.toInt() } }
    println(ChickenDelivery(N, map).solve(M))
}
