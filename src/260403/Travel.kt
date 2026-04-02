package `260403`

import kotlin.math.max

fun main() {
    // 안전한 입력 파싱 헬퍼 함수
    fun readInts() = readln().split(" ").mapNotNull { it.toIntOrNull() }

    val (n, m, k) = readInts()

    // 1. 인접 행렬 구성 (repeat을 활용한 직관적인 초기화)
    val adj = Array(n + 1) { IntArray(n + 1) }

    repeat(k) {
        val (a, b, c) = readInts()
        // "서쪽으로만 이동" (번호가 증가하는 방향)
        adj[a][b] = max(adj[a][b], c) // 동일 경로 시 최대 기내식 점수 유지
    }

    // 2. DP 초기 상태 설정 (1번 도시에서 시작)
    val initialDp = IntArray(n + 1) { if (it == 1) 0 else -1 }

    // 3. DP 전개
    val answer = (2..m).fold(initialDp to 0) { (prev, maxAtN), _ ->
        // ==============================================================================
        //   prev[i]       == DP[현재 방문 횟수 - 1][i 번 도시]
        //   current[next] == DP[현재 방문 횟수][next 번 도시]
        //
        // 점화식:
        //   current[next] = MAX( prev[i] + adj[i][next] )
        //
        // 조건:
        //   1. 1 <= i < next (서쪽으로만 이동하므로 이전 도시 i는 next보다 작아야 함)
        //   2. prev[i] != -1 (이전 단계에서 i번 도시에 도달할 수 있어야 함)
        //   3. adj[i][next] > 0 (i에서 next로 가는 항로가 존재해야 함)
        // ==============================================================================
        val current = IntArray(n + 1) { next ->
            if (next <= 1) -1
            else (1 until next).fold(-1) { maxVal, i ->
                if (prev[i] != -1 && adj[i][next] > 0) {
                    max(maxVal, prev[i] + adj[i][next])
                } else {
                    maxVal
                }
            }
        }

        // 현재까지 N번 도시에 도달한 값 중 최댓값을 갱신하며 다음 단계로 전달
        current to max(maxAtN, current[n])
    }.second

    println(answer)
}
