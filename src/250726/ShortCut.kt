package `250726`

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val dp = Array(10001) { i -> i }

    val graph: Map<Int, List<Pair<Int, Int>>> = (0..<N).mapNotNull {
        val (start, end, shortCutDistance) = readln().split(" ").map { it.toInt() }
        if (end <= M && (end - start) > shortCutDistance) {
            end to Pair(start, shortCutDistance)
        } else null
    }.groupBy({ it.first }, { it.second })

    (1..M).forEach { end ->
        dp[end] = dp[end - 1] + 1
        dp[end] = graph[end]?.minOf {
            dp[it.first] + it.second
        }?.takeIf { it < dp[end] } ?: dp[end]
    }

    println(dp[M])
}
