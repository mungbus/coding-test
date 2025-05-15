package `250516`

import kotlin.math.min

fun main() {
    val N = readln().toInt()
    repeat(N) {
        val fileLength = readln().toInt()
        val fileSizes = readln().split(" ").map { it.toInt() }
        val sum = IntArray(fileLength + 1) { 0 }
        for (i in 1..fileLength) {
            sum[i] = sum[i - 1] + fileSizes[i - 1]
        }

        val dp = Array(fileLength) { IntArray(fileLength) { Int.MAX_VALUE / 2 } }
        for (i in 0..<fileLength) {
            dp[i][i] = 0
        }

        for (length in 2..fileLength) {
            for (start in 0..fileLength - length) {
                val end = start + length - 1
                for (mid in start..<end) {
                    dp[start][end] = min(dp[start][end], dp[start][mid] + dp[mid + 1][end])
                }
                dp[start][end] += sum[end + 1] - sum[start]
            }
        }

        println(dp[0][fileLength - 1])
    }
}
