package `251228`

import kotlin.math.abs

fun main() {
    readln().toInt() // n 입력값 읽음
    val positions = readln().split(" ").map { it.toLong() }
    val costs = readln().split(" ").map { it.toInt() }
    val b = readln().toLong()

    // 폭탄을 위치 기준으로 정렬하고 원본 인덱스 저장
    val bombs = positions.mapIndexed { idx, pos -> Triple(pos, costs[idx], idx) }
        .sortedBy { it.first }

    // 이진 탐색으로 최소 R 찾기
    var left = 0L
    var right = 4 * 10_000_000_000L // 최대 거리 예상
    var answer = right

    while (left <= right) {
        val mid = (left + right) / 2
        if (canDestroyAll(bombs, mid, b)) {
            answer = mid
            right = mid - 1
        } else {
            left = mid + 1
        }
    }

    println(answer)
}

fun canDestroyAll(bombs: List<Triple<Long, Int, Int>>, r: Long, budget: Long): Boolean {
    val n = bombs.size

    // DP: dp[i] = i번째 폭탄까지 모두 터뜨렸을 때의 최소 비용
    val dp = LongArray(n + 1) { Long.MAX_VALUE }
    dp[0] = 0L

    for (i in 0 until n) {
        if (dp[i] == Long.MAX_VALUE) continue

        // i번째 폭탄부터 시작하는 폭탄 선택
        for (start in i until n) {
            val cost = bombs[start].second.toLong()
            if (dp[i] + cost > budget) continue

            // start 폭탄을 선택했을 때의 연쇄 폭발 계산
            val destroyed = BooleanArray(n)
            val queue = mutableListOf<Int>()
            val visited = BooleanArray(n)

            queue.add(start)
            visited[start] = true
            destroyed[start] = true

            var qIdx = 0
            while (qIdx < queue.size) {
                val curr = queue[qIdx]
                qIdx++
                val currPos = bombs[curr].first

                for (j in 0 until n) {
                    if (!visited[j] && abs(bombs[j].first - currPos) <= r) {
                        visited[j] = true
                        queue.add(j)
                        destroyed[j] = true
                    }
                }
            }

            // 다음에 처리할 폭탄 찾기
            var nextIdx = i
            while (nextIdx < n && destroyed[nextIdx]) {
                nextIdx++
            }

            dp[nextIdx] = minOf(dp[nextIdx], dp[i] + cost)
        }
    }

    return dp[n] <= budget
}

