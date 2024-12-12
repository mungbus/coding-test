package `18week`

class Solution {
    fun countSquares(matrix: Array<IntArray>): Int {
        val rows = matrix.size
        val cols = matrix[0].size

        val dp = Array(rows) { IntArray(cols) { 0 } }
        var answer = 0
        for (i in 0..<rows) {
            for (j in 0..<cols) {
                if (matrix[i][j] == 1) {
                    dp[i][j] = if (i == 0 || j == 0) {
                        1
                    } else {
                        minOf(
                            dp[i - 1][j],
                            dp[i][j - 1],
                            dp[i - 1][j - 1]
                        ) + 1
                    }
                    answer += dp[i][j]
                }
            }
        }
        return answer
    }
}

fun main() {
    val result = Solution().countSquares(
        arrayOf(
            intArrayOf(0, 1, 1, 1),
            intArrayOf(1, 1, 1, 1),
            intArrayOf(0, 1, 1, 1)
        )
    )
    println(result)
}
