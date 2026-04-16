package `260417`

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer

fun Pair<Int, Int>.toLength(): Long {
    return (second - first).toLong()
}

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val n = br.readLine().toInt()

    // Pair 객체를 담을 리스트 (메모리 효율을 위해 초기 용량 설정)
    val lines = Array(n) {
        val st = StringTokenizer(br.readLine())
        val x = st.nextToken().toInt()
        val y = st.nextToken().toInt()
        // 입력받은 즉시 x < y 상태를 보장하며 저장
        if (x > y) y to x else x to y
    }

    // 1. 시작점(first) 기준 오름차순 정렬
    lines.sortBy { it.first }
    val iterator = lines.iterator()

    var totalLength = 0L
    var current = iterator.next()

    // 2. 라인 스위핑
    iterator.forEach { next ->
        if (next.first <= current.second) {
            // 겹치거나 이어지는 경우
            current = current.first to maxOf(current.second, next.second)
        } else {
            // 끊긴 경우
            totalLength += current.toLength()
            current = next
        }
    }

    // 마지막 선분 계산
    totalLength += current.toLength()

    println(totalLength)
}
