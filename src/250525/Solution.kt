package `250525`

import kotlin.math.abs

/*
입력 배열을 정렬합니다.
양 끝에서 투 포인터로 합의 절댓값이 최소가 되는 쌍을 찾습니다.
*/

fun main() {
    readln()
    val solList = readln().split(" ").map { it.toInt() }.sorted()
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
