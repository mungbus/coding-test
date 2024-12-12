package `18week`

import kotlin.math.min

fun main() {
    val (N, score, P) = readln().split(" ").map { it.toInt() }
    val scores = if (N == 0) emptyList() else readln().split(" ").map { it.toInt() }
    var topMin = -1
    val currentTop = scores.filter {
        val check = it >= score
        if (check) {
            topMin = if (topMin == -1) it else min(topMin, it)
        }
        check
    }
    if (currentTop.size < P) {
        val beforeRank = currentTop.filter { if (topMin == score) it != topMin else true }.size
        println(beforeRank + 1)
    } else {
        println(-1)
    }
}
