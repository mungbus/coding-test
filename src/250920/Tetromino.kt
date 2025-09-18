package `250920`

class Tetromino(val n: Int, private val m: Int, private val board: Array<IntArray>) {
    inner class Position(val x: Int, val y: Int) {
        operator fun plus(other: Position): Position {
            return Position(x + other.x, y + other.y)
        }

        fun isValid(): Boolean {
            return x in 0..<n && y in 0..<m
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Position) return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            return 31 * x + y
        }
    }

    private fun List<Position>.rotate(): List<Position> {
        val maxX = maxOf { it.x }
        return map { Position(it.y, maxX - it.x) }
    }

    private fun List<Position>.mirror(): List<Position> {
        val maxY = maxOf { it.y }
        return map { Position(it.x, maxY - it.y) }
    }

    private fun generateTransformations(shape: List<Position>): List<List<Position>> {
        val transformations = mutableSetOf<List<Position>>()
        var current = shape
        repeat(4) {
            transformations.add(current)
            transformations.add(current.mirror())
            current = current.rotate()
        }
        return transformations.toList()
    }

    private val shapes = listOf(
        listOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)), // ㅡ
        listOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1)), // ㅁ
        listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1)), // ㄴ
        listOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 1)), // ㅜ
        listOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 1))  // Z
    ).flatMap { generateTransformations(it) }

    private fun calculateShapeSum(start: Position, shape: List<Position>): Int {
        return shape.sumOf { pos ->
            val newPos = start + pos
            if (!newPos.isValid()) return 0
            board[newPos.x][newPos.y]
        }
    }

    fun solve(): Int {
        var maxSum = 0
        repeat(n) { i ->
            repeat(m) { j ->
                val start = Position(i, j)
                shapes.forEach {
                    maxSum = maxOf(maxSum, calculateShapeSum(start, it))
                }
                maxSum = maxOf(maxSum, board[i][j])
            }
        }
        return maxSum
    }
}

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val board = Array(N) { readln().split(" ").map { it.toInt() }.toIntArray() }
    println(Tetromino(N, M, board).solve())
}
