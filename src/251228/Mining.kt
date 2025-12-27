package `251228`

import java.util.LinkedList
import java.util.Queue
import java.util.StringTokenizer
import kotlin.math.max
import kotlin.math.min

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

fun main() {
    val (N, M, K) = readln().split(" ").map { it.toInt() }

    val map = Array(N) { IntArray(M) }
    var minVal = Int.MAX_VALUE
    var maxVal = Int.MIN_VALUE

    repeat(N) { r ->
        val st = StringTokenizer(readln())
        var c = 0
        while (st.hasMoreTokens()) {
            val value = st.nextToken().toInt()
            map[r][c++] = value
            minVal = min(minVal, value)
            maxVal = max(maxVal, value)
        }
    }

    var low = minVal
    var high = maxVal
    var answer = maxVal

    // BFS를 위한 방향 벡터 (상, 하, 좌, 우)
    val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    // 방문 체크 배열 재사용 (메모리 할당 최적화)
    val visited = Array(N) { IntArray(M) }
    var visitToken = 0

    fun canMine(limit: Int): Boolean {
        visitToken++
        val queue: Queue<Pair<Int, Int>> = LinkedList()
        var count = 0

        // 테두리 확인 및 큐에 추가 (최적화: 전체 순회 -> 테두리 순회)
        // 1. 윗면 (r=0)
        for (c in 0 until M) {
            if (map[0][c] <= limit && visited[0][c] != visitToken) {
                visited[0][c] = visitToken
                queue.offer(0 to c)
                count++
            }
        }

        // 2. 좌우 측면 (r=1..N-1)
        for (r in 1 until N) {
            // 좌측
            if (map[r][0] <= limit && visited[r][0] != visitToken) {
                visited[r][0] = visitToken
                queue.offer(r to 0)
                count++
            }
            // 우측 (M > 1 체크로 중복 방지)
            if (M > 1 && map[r][M - 1] <= limit && visited[r][M - 1] != visitToken) {
                visited[r][M - 1] = visitToken
                queue.offer(r to M - 1)
                count++
            }
        }

        if (count >= K) return true

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            directions.forEach { dir ->
                val (nr, nc) = current + dir

                if (nr in 0 until N && nc in 0 until M) {
                    if (visited[nr][nc] != visitToken && map[nr][nc] <= limit) {
                        visited[nr][nc] = visitToken
                        queue.offer(nr to nc)
                        count++
                        if (count >= K) return true // 조기 종료
                    }
                }
            }
        }

        return false
    }

    while (low <= high) {
        val mid = (low + high) / 2
        if (canMine(mid)) {
            answer = mid
            high = mid - 1
        } else {
            low = mid + 1
        }
    }

    println(answer)
}
