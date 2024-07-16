package `5week`

fun main() {
    val N = readln().toInt()
    val dp = IntArray(31).apply {
        fill(0)
        this[0] = 1
        this[2] = 3
    }
    // 2 -> 3
    // 4 -> 9 + 2
    // 6 ->
    // 항상 2의 배수만 됨
    if (N % 2 == 0) {
        var i = 4
        while (i <= N) {
            var j = 0
            while (j < i - 2) {
                dp[i] += dp[j] * 2
                j += 2
            }
            dp[i] += dp[i - 2] * dp[2]
            i += 2
        }
        println(dp[N])
    } else println(0)
}
