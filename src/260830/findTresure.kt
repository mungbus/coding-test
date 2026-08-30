package `260830`

class Solution {
    /**
     * [전체 동작 플로우]
     * 1. 전처리 (Precomputation):
     *    - TreasureFinder 인스턴스 생성 시 init 블록에서 O(w³) 구간 DP 실행.
     *    - 모든 부분 구간 [l, r]에 대해 최악 비용(Minimax)을 최소화하는 최적 열 optimalChoice[l][r] 사전 계산.
     *
     * 2. 대화형 탐색 (Interactive Search):
     *    - 전체 구간 [1, n]에서 시작하여 optimalChoice[l][r]가 가리키는 열을 excavate(col)로 굴착.
     *    - 0  -> 보물 발견: 탐색 즉시 종료 및 해당 열 반환.
     *    - -1 -> 보물이 왼쪽: current.toLeftOf(col)로 [left, col - 1] 서브 구간 전이.
     *    - 1  -> 보물이 오른쪽: current.toRightOf(col)로 [col + 1, right] 서브 구간 전이.
     *    - tailrec 꼬리 재귀를 통해 스택 오버플로우 없이 O(log w) 굴착 횟수로 확정 발굴.
     */
    fun solution(depth: IntArray, money: Int, excavate: (Int) -> Int): Int {
        val finder = TreasureFinder(depth)
        return finder.findTreasure(excavate)
    }
}

/**
 * 탐색 대상 구간 [left, right]을 표현하는 도메인 데이터 클래스
 */
data class SearchRange(val left: Int, val right: Int) {
    /**
     * 특정 열(target) 기준 좌측 서브 구간 [left, target - 1] 생성
     */
    fun toLeftOf(target: Int): SearchRange = SearchRange(left, target - 1)

    /**
     * 특정 열(target) 기준 우측 서브 구간 [target + 1, right] 생성
     */
    fun toRightOf(target: Int): SearchRange = SearchRange(target + 1, right)
}

class TreasureFinder(private val depth: IntArray) {
    private val n = depth.size

    // dp[l][r]: l열부터 r열 사이에 보물이 있을 때, 100% 확정/발굴하기 위한 최악 비용의 최솟값
    private val dp = Array(n + 2) { IntArray(n + 2) }

    // optimalChoice[l][r]: 구간 [l, r]에서 최악 비용을 최소화하는 최적의 굴착 위치 k
    private val optimalChoice = Array(n + 2) { IntArray(n + 2) }

    init {
        // 객체 생성 즉시 1회성 전처리 수행
        buildOptimalStrategy()
    }

    /**
     * 구간 DP (Minimax) 사전 연산
     * 점화식: DP(l, r) = min_{l <= k <= r} ( depth[k-1] + max(DP(l, k-1), DP(k+1, r)) )
     */
    private fun buildOptimalStrategy() {
        // 구간 길이(len)를 1부터 전체 열 크기(n)까지 점진적으로 확장 (Bottom-Up)
        for (len in 1..n) {
            for (l in 1..(n - len + 1)) {
                val r = l + len - 1
                var minCost = Int.MAX_VALUE
                var bestK = l

                // 구간 [l, r] 내에서 최악의 경우에도 비용이 최소가 되는 최적 열 k 탐색
                for (k in l..r) {
                    val cost = depth[k - 1] + maxOf(dp[l][k - 1], dp[k + 1][r])
                    if (cost < minCost) {
                        minCost = cost
                        bestK = k
                    }
                }

                dp[l][r] = minCost
                optimalChoice[l][r] = bestK
            }
        }
    }

    /**
     * 사전 계산된 최적 전략(optimalChoice)을 기반으로 대화형 발굴 수행
     */
    fun findTreasure(excavate: (Int) -> Int): Int {
        // tailrec을 활용해 탐색 범위를 좁혀가며 스택 오버플로우 없이 안전하게 재귀 수행
        tailrec fun search(current: SearchRange): Int {
            // 현재 구간에 대한 최적 굴착 위치 조회 (O(1))
            val targetCol = optimalChoice[current.left][current.right]

            return when (excavate(targetCol)) {
                0 -> targetCol // 보물 발견 및 굴착 완료 (종료 조건)
                -1 -> search(current.toLeftOf(targetCol)) // 보물이 왼쪽에 있음 -> [left, targetCol - 1] 탐색
                1 -> search(current.toRightOf(targetCol)) // 보물이 오른쪽에 있음 -> [targetCol + 1, right] 탐색
                else -> throw IllegalStateException("Invalid response from excavate")
            }
        }

        // 1번부터 n번 열까지 전체 구간에서 탐색 시작
        return search(SearchRange(1, n))
    }
}

data class TestCase(
    val depth: IntArray,
    val money: Int,
    val actualTreasure: Int
)

fun main() {
    val testCases = listOf(
        TestCase(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 55, 3),
        TestCase(intArrayOf(1, 1, 1, 1, 1), 3, 5),
        TestCase(intArrayOf(2, 100, 1, 100, 3, 100, 1), 200, 6),
        TestCase(intArrayOf(2, 100, 1, 100, 3, 100, 1), 200, 5),
        TestCase(intArrayOf(3, 2, 1, 2, 3, 2, 1, 2), 8, 5),
        TestCase(intArrayOf(1, 1000, 1, 1, 1, 10, 15, 1), 1002, 2)
    )

    val solutionInstance = Solution()

    testCases.forEachIndexed { index, tc ->
        var totalCostUsed = 0
        var foundZero = false

        // 실제 보물 위치(actualTreasure)와 굴착 비용을 시뮬레이션하는 excavate 람다
        val excavateMock: (Int) -> Int = { col ->
            totalCostUsed += tc.depth[col - 1]
            when {
                col == tc.actualTreasure -> {
                    foundZero = true
                    0
                }

                col > tc.actualTreasure -> -1
                else -> 1
            }
        }

        val returnedResult = solutionInstance.solution(tc.depth, tc.money, excavateMock)

        val isCorrectCol = (returnedResult == tc.actualTreasure)
        val isUnderMoney = (totalCostUsed <= tc.money)
        val isPassed = isCorrectCol && isUnderMoney && foundZero

        println("[테스트 케이스 ${index + 1}] 결과: ${if (isPassed) "PASS ✅" else "FAIL ❌"}")
        println(" - 반환 위치: $returnedResult (정답: ${tc.actualTreasure})")
        println(" - 사용 비용: $totalCostUsed / ${tc.money}")
        println(" - 0(발견) 신호 수신 여부: $foundZero")
        println("-".repeat(40))
    }
}
