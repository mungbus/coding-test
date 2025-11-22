package `251123`

fun main() {
    while (true) {
        val N = readln().trim().toInt()
        if (N == 0) break
        val graph = Array(N + 1) { IntArray(N + 1) }

        // n+1줄의 입력을 읽어 거리 행렬에 저장
        repeat(N + 1) { i ->
            val line = readln().split(" ").map { it.toInt() }
            repeat(N + 1) { j ->
                if (j < line.size) graph[i][j] = line[j]
            }
        }

        // Floyd-Warshall 알고리즘으로 모든 쌍 간 최단 경로 계산
        repeat(N) { k ->
            repeat(N) { i ->
                repeat(N) { j ->
                    if (graph[i][k] + graph[k][j] < graph[i][j]) {
                        graph[i][j] = graph[i][k] + graph[k][j]
                    }
                }
            }
        }

        val locations = (1..N).toList()
        // 최소 시간을 저장할 변수, 초기값은 최대 정수
        var minTime = Int.MAX_VALUE

        // TSP를 위한 순열 생성 재귀 함수
        fun permute(current: List<Int>, remaining: List<Int>) {
            // 모든 위치를 방문한 경우 경로 비용 계산
            if (remaining.isEmpty()) {
                // 0에서 첫 번째 위치로의 시간
                var time = graph[0][current[0]]
                // 순열 내 연속 위치 간 이동 시간 더하기
                repeat(current.size - 1) { i ->
                    time += graph[current[i]][current[i + 1]]
                }
                // 마지막 위치에서 0으로 돌아오는 시간 더하기
                time += graph[current.last()][0]
                // 최소 시간 업데이트
                if (time < minTime) minTime = time
                return
            }
            // 남은 위치 중 하나를 선택해 재귀 호출
            remaining.indices.forEach {
                val next = remaining[it]
                val newRemaining = remaining.filterIndexed { index, _ -> index != it }
                permute(current + next, newRemaining)
            }
        }

        permute(emptyList(), locations)
        println(minTime)
    }
}
