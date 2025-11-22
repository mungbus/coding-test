package `251123`

import java.util.Scanner
import kotlin.math.min

const val INF = 1_000_000_000

fun main() {
    val sc = Scanner(System.`in`)
    while (sc.hasNextInt()) {
        val n = sc.nextInt()
        if (n == 0) break // n=0이면 종료

        // 거리 행렬 입력 (0번은 피자집, 1~n번은 배달 장소)
        // 크기는 (n+1) x (n+1)
        val dist = Array(n + 1) { IntArray(n + 1) }
        for (i in 0..n) {
            for (j in 0..n) {
                dist[i][j] = sc.nextInt()
            }
        }

        // Floyd-Warshall
        // 모든 정점 쌍 사이의 최단 거리를 먼저 갱신
        for (k in 0..n) {
            for (i in 0..n) {
                for (j in 0..n) {
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
                }
            }
        }

        // 백트래킹 (DFS) 탐색
        var minResult = INF
        val visited = BooleanArray(n + 1) // 방문 여부 체크용 배열

        fun dfs(current: Int, count: Int, currentCost: Int) {
            // 현재 비용이 이미 찾은 최소값보다 크면 더 가볼 필요 없음
            if (currentCost >= minResult) return

            if (count == n) {
                // 모든 배달 장소(n개)를 방문했을 때 현재 위치에서 다시 피자집(0)으로 돌아가는 비용 추가 후 갱신
                minResult = min(minResult, currentCost + dist[current][0])
                return
            }

            // 다음 방문할 장소 탐색 (1번부터 n번까지)
            for (next in 1..n) {
                if (!visited[next]) {
                    visited[next] = true // 방문 처리

                    // 다음 장소로 이동
                    dfs(next, count + 1, currentCost + dist[current][next])

                    visited[next] = false // 원상 복구 (백트래킹)
                }
            }
        }

        dfs(0, 0, 0)

        println(minResult)
    }
}
