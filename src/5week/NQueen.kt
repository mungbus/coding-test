package `5week`

import kotlin.math.abs

class NQueen(private val N: Int) {
    private val chessMap: IntArray = IntArray(N)

    init {
        recursive(0)
    }

    var answer: Int = 0
    fun recursive(i: Int) {
        if (i == N) {
            answer++
            return
        }

        (0 until N).forEach {
            chessMap[i] = it
            if (check(i)) {
                recursive(i + 1)
            }
        }
    }

    fun check(col: Int): Boolean {
        (0 until col).forEach {
            if (chessMap[col] == chessMap[it] || abs((col - it).toDouble()) == abs((chessMap[col] - chessMap[it]).toDouble())) {
                return false
            }
        }
        return true
    }
}

fun main() {
    val N = readln().toInt()
    println(NQueen(N).answer)
}
