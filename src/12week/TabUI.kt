package `12week`

import java.math.RoundingMode
import java.text.DecimalFormat

val df = DecimalFormat("0.00").apply {
    roundingMode = RoundingMode.DOWN
}

fun main() {
    val N = readln().toInt()
    val prefixSum = Array(N + 1) { 0 }
    val coordinate = Array(N + 1) { 0.0 }
    repeat(N) {
        val tab = readln().toInt()
        prefixSum[it + 1] = prefixSum[it] + tab
        coordinate[it + 1] = prefixSum[it + 1] - tab.toDouble() / 2
    }
    val L = readln().toInt()
    val Q = readln().toInt()
    val mid = L.toDouble() / 2
    val answer = Array(Q) {
        val click = readln().toInt()
        val gap = mid - coordinate[click]
        val value = if (gap >= 0 || prefixSum[N] < L) 0.0
        else if (prefixSum[N].toDouble() - coordinate[click] < L.toDouble() / 2) prefixSum[N].toDouble() - L
        else -gap

        df.format(value)
    }
    println(answer.joinToString("\n"))
}
