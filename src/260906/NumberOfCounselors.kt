package `260906`

import java.util.PriorityQueue

fun main() {
    val solution = Solution()

    // 입출력 예 #1
    val k1 = 3
    val n1 = 5
    val reqs1 = arrayOf(
        intArrayOf(10, 60, 1),
        intArrayOf(15, 100, 3),
        intArrayOf(20, 30, 1),
        intArrayOf(30, 50, 3),
        intArrayOf(50, 40, 1),
        intArrayOf(60, 30, 2),
        intArrayOf(65, 30, 1),
        intArrayOf(70, 100, 2)
    )
    val result1 = solution.solution(k1, n1, reqs1)
    println("예제 1 결과: $result1 (기댓값: 25)")

    // 입출력 예 #2
    val k2 = 2
    val n2 = 3
    val reqs2 = arrayOf(
        intArrayOf(5, 55, 2),
        intArrayOf(10, 90, 2),
        intArrayOf(20, 40, 2),
        intArrayOf(50, 45, 2),
        intArrayOf(100, 50, 2)
    )
    val result2 = solution.solution(k2, n2, reqs2)
    println("예제 2 결과: $result2 (기댓값: 90)")
}

class Solution {

    // [개선 포인트 1] 상태와 행위의 캡슐화: 상담 유형을 객체로 정의
    class ConsultCategory {
        val requests = ArrayList<IntArray>()

        // 동일한 멘토 수에 대한 중복 계산을 방지하기 위한 캐시 메모리
        private val memo = HashMap<Int, Int>()

        // 특정 멘토 수가 주어졌을 때의 최소 대기 시간을 계산 (지연 평가)
        fun getMinWaitTime(mentors: Int): Int {
            // 요청이 없거나, 멘토가 요청 수 이상이면 대기할 필요가 없으므로 0 반환
            if (requests.isEmpty() || mentors >= requests.size) return 0

            // 이미 계산해 둔 멘토 수라면 바로 반환 (getOrPut 활용)
            return memo.getOrPut(mentors) {
                val pq = PriorityQueue<Int>()
                repeat(mentors) { pq.add(0) } // 모든 멘토를 0분(가용 상태)으로 초기화
                var totalWait = 0

                // 구조 분해 선언: start(시작 시각), duration(상담 시간), 3번째 원소는 미사용(_)
                requests.forEach { (start, duration, _) ->
                    val earliestEnd = pq.poll()

                    if (earliestEnd <= start) {
                        // 멘토가 이미 대기 중이면 도착 즉시 상담 시작
                        pq.add(start + duration)
                    } else {
                        // 모든 멘토가 상담 중이면 가장 빨리 끝나는 시간까지 대기
                        totalWait += earliestEnd - start
                        pq.add(earliestEnd + duration)
                    }
                }
                totalWait // 계산된 누적 대기 시간 반환
            }
        }
    }

    fun solution(k: Int, n: Int, reqs: Array<IntArray>): Int {
        // 1. 객체 배열 초기화 및 각 요청을 알맞은 상담 유형 객체에 분배 (O(N))
        val categories = Array(k + 1) { ConsultCategory() }
        reqs.forEach { req ->
            categories[req[2]].requests.add(req)
        }

        // [개선 포인트 2] 1차원 DP 배열 및 Int.MAX_VALUE 사용
        // dp[total]: 현재 단계까지 총 total명의 멘토를 배정했을 때의 최소 대기 시간
        val dp = IntArray(n + 1) { Int.MAX_VALUE }

        // 2. 1번 상담 유형에 대한 초기 기저 사례 세팅
        // 1번 유형이 가질 수 있는 최대 멘토 수 = 전체 n명 - 나머지 유형(k-1개)에 최소 1명씩
        val maxMentorsForFirst = n - k + 1
        for (m in 1..maxMentorsForFirst) {
            dp[m] = categories[1].getMinWaitTime(m)
        }

        // 3. 2번 유형부터 k번 유형까지 1차원 배열 하나로 덮어쓰기(In-place) 갱신
        for (type in 2..k) {
            val maxTotalMentors = n - (k - type) // 남은 유형을 배려해 현재까지 쓸 수 있는 최대 멘토 수
            val minTotalMentors = type           // 각 유형당 최소 1명씩은 받아야 하므로 최소 type명 필요

            // [개선 포인트 3] 뒤에서부터 갱신(downTo)하여 이전 단계의 값이 훼손되는 것을 방지
            for (total in maxTotalMentors downTo minTotalMentors) {
                var minWait = Int.MAX_VALUE

                // 현재 유형(type)에 줄 수 있는 멘토 수(m)의 상한선
                // 총 total명 중 이전 유형들(type-1개)이 최소 1명씩은 가져가야 함
                val maxM = total - (type - 1)

                for (m in 1..maxM) {
                    // Int.MAX_VALUE에 값을 더해 음수로 오버플로우가 발생하는 것을 방지
                    if (dp[total - m] != Int.MAX_VALUE) {
                        val currentWait = dp[total - m] + categories[type].getMinWaitTime(m)
                        minWait = minOf(minWait, currentWait)
                    }
                }
                // 탐색된 최솟값으로 현재 인덱스의 값만 깔끔하게 덮어씀
                dp[total] = minWait
            }
        }

        // 총 n명의 멘토를 k개 유형에 모두 배정했을 때의 최소 대기 시간 반환
        return dp[n]
    }
}
