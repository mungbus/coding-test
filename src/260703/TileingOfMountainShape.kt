package `260703`

class Solution {
    fun solution(n: Int, tops: IntArray): Int {
        val mod = 10007

        // free:
        // 현재 위쪽 삼각형 T_i가 왼쪽 아래 삼각형 B_(i-1)과
        // 마름모를 만들 수 있는 상태의 수
        var free = 1

        // blocked:
        // 왼쪽 아래 삼각형 B_(i-1)이 이미 이전 칸에서 사용되어,
        // 현재 T_i가 왼쪽 아래와 마름모를 만들 수 없는 상태의 수
        var blocked = 0

        for (i in 0 until n) {
            // currentWays:
            // 현재 T_i를 보기 직전까지 만들어진 전체 경우의 수입니다.
            // free/blocked 둘 다 가능한 선택지를 처리할 때 사용합니다.
            // 예: T_i 혼자 채우기, 위쪽과 묶기, 오른쪽 아래와 묶기
            val currentWays = (free + blocked) % mod
            var nextFree = 0
            var nextBlocked = 0

            // 선택 1. T_i를 정삼각형 타일 하나로 채우기
            // 조건: 항상 가능
            // 다음 상태: 오른쪽 아래 B_i를 사용하지 않으므로 nextFree
            nextFree = (nextFree + currentWays) % mod

            // 선택 2. T_i를 왼쪽 아래 B_(i-1)와 마름모로 채우기
            // 조건: 왼쪽 아래를 쓸 수 있는 free 상태에서만 가능
            // 다음 상태: 오른쪽 아래 B_i를 사용하지 않으므로 nextFree
            nextFree = (nextFree + free) % mod

            // 선택 3. T_i를 위쪽 추가 삼각형과 마름모로 채우기
            // 조건: tops[i] == 1일 때만 가능
            // 다음 상태: 오른쪽 아래 B_i를 사용하지 않으므로 nextFree
            if (tops[i] == 1) {
                nextFree = (nextFree + currentWays) % mod
            }

            // 선택 4. T_i를 오른쪽 아래 B_i와 마름모로 채우기
            // 조건: 현재 free/blocked 여부와 상관없이 항상 가능
            // 다음 상태: B_i를 사용했으므로 다음 칸은 nextBlocked
            nextBlocked = (nextBlocked + currentWays) % mod

            free = nextFree
            blocked = nextBlocked
        }

        return (free + blocked) % mod
    }
}

fun main() {
    val solution = Solution()
    println(solution.solution(4, intArrayOf(1, 1, 0, 1)))
    println(solution.solution(2, intArrayOf(0, 1)))
    println(solution.solution(10, intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)))
}
