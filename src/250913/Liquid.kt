package `250913`

import kotlin.math.abs

fun main() {
    readln()
    val solList = readln().split(" ").map { it.toInt() }
    var left = 0
    var right = solList.lastIndex
    var min = Int.MAX_VALUE
    var answer = Pair(0, 0)

    while (left < right) {
        val sum = solList[left] + solList[right]
        if (abs(sum) < min) {
            min = abs(sum)
            answer = Pair(solList[left], solList[right])
        }
        if (sum < 0) left++ else right--
    }
    println(listOf(answer.first, answer.second).sorted().joinToString(" "))
}
