package `21week`

var second = 0

fun addBomb(board: Array<Array<Int>>, second: Int) {
    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j] == -1) {
                board[i][j] = second
            }
        }
    }
}

fun main() {
    val (R, C, N) = readln().split(" ").map { it.toInt() }
    Array(R) {
        readln().split("").map { if (it == "O") second else -1 }
    }
}
