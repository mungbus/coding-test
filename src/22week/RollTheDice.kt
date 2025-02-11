package `22week`

data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)

    fun getValue(board: Array<IntArray>) = board[x][y]
    fun setValue(board: Array<IntArray>, value: Int) {
        board[x][y] = value
    }
}

fun main() {
    val (N, M, x, y) = readln().split(" ").map { it.toInt() }
    var pos = Pos(x, y)
    val board = Array(N) { readln().split(" ").map { it.toInt() }.toIntArray() }
    val commands = readln().split(" ").map { it.toInt() }

    val dice = IntArray(6)
    val direction = arrayOf(Pos(0, 1), Pos(0, -1), Pos(-1, 0), Pos(1, 0))

    var top = 0
    var bottom = 1
    var north = 2
    var south = 3
    var west = 4
    var east = 5

    for (command in commands) {
        val newPos = pos + direction[command - 1]
        if (newPos.x < 0 || newPos.x >= N || newPos.y < 0 || newPos.y >= M) {
            continue
        }
        pos = newPos
        when (command) {
            1 -> {
                val temp = top
                top = west
                west = bottom
                bottom = east
                east = temp
            }

            2 -> {
                val temp = top
                top = east
                east = bottom
                bottom = west
                west = temp
            }

            3 -> {
                val temp = top
                top = south
                south = bottom
                bottom = north
                north = temp
            }

            4 -> {
                val temp = top
                top = north
                north = bottom
                bottom = south
                south = temp
            }
        }
        if (pos.getValue(board) == 0) {
            pos.setValue(board, dice[bottom])
        } else {
            dice[bottom] = pos.getValue(board)
            pos.setValue(board, 0)
        }
        println(dice[top])
    }
}
