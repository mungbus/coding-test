package `13week`

import java.math.BigDecimal

class Solution {
    fun solution(numbers: IntArray): String {
        val sorted = numbers.map { it.toString() }.sortedWith { a, b -> (b + a).compareTo(a + b) }
        val result = sorted.joinToString("")
        return BigDecimal(result).toString()
    }
}

fun main() {
    val s = Solution()
    println(s.solution(intArrayOf(0, 0, 0, 0)) == "0")
    println(s.solution(intArrayOf(0, 0)) == "0")
    println(s.solution(intArrayOf(0, 0, 1)) == "100")
    println(s.solution(intArrayOf(0, 1, 0, 0)) == "1000")
    println(s.solution(intArrayOf(3, 34, 30)) == "34330")
    println(s.solution(intArrayOf(6, 10, 2)) == "6210")
    println(s.solution(intArrayOf(3, 30, 34, 5, 9)) == "9534330")
    println(s.solution(intArrayOf(999, 112, 444, 222)) == "999444222112")
}
