package `6week`

import kotlin.math.sqrt

class Carpet {
    fun solution(brown: Int, yellow: Int): IntArray {
        // x * y = (brown + yellow)
        // (x + y) * 2 - 4 = brown

        // y = (brown + 4) / 2 - x
        // x * ((brown + 4) / 2 - x) = brown + yellow
        val sqrt = Math.ceil(sqrt(brown.toDouble() + yellow.toDouble())).toInt()
        for (i in (3..sqrt)) {
            if (i * ((brown + 4) / 2 - i) == brown + yellow) {
                return intArrayOf(i, (brown + yellow) / i).sortedArrayDescending()
            }
        }
        return intArrayOf()
    }
}

fun main() {
    println(Carpet().solution(10, 2).toList())
    println(Carpet().solution(24, 24).toList())
}
