package `260306`

/**
 * 학생들의 선택 관계를 통해 팀을 구성하는 문제
 *
 * 알고리즘 설명:
 * - 각 학생은 정확히 한 명의 다른 학생(또는 자신)을 선택
 * - 팀이 되려면 선택 관계가 사이클을 형성해야 함
 * - 예: A→B→C→A (사이클), 또는 A→A (자기 선택)
 * - 사이클에 속하지 않는 학생들의 수를 구함
 *
 * 시간복잡도: O(n) - 각 노드는 최대 1회만 방문
 * 공간복잡도: O(n) - visited, inCycle, pathIdx 배열/맵 사용
 */
fun main() {
    val T = readln().toInt()

    repeat(T) {
        val n = readln().toInt()
        // choices[i] = i번째 학생이 선택한 학생 번호 (1-indexed)
        val choices = readln().split(" ").map { it.toInt() }

        // inCycle[i] = i번째 학생이 사이클에 속하는지 여부
        val inCycle = BooleanArray(n)
        // visited[i] = i번째 학생이 이미 처리되었는지 여부
        val visited = BooleanArray(n)

        // 모든 학생에 대해 사이클 탐지
        repeat(n) { start ->
            // 이미 처리된 학생은 스킵
            if (visited[start]) return@repeat

            // 현재 경로의 노드들과 그 인덱스를 저장 (삽입 순서 유지)
            val pathIdx = linkedMapOf<Int, Int>()
            var current = start

            // 경로를 따라가며 사이클 찾기
            // - visited[current]가 true: 이전에 처리된 노드 도달
            // - current in pathIdx: 현재 경로 내에서 반복되는 노드 도달 (사이클 발견!)
            while (!visited[current] && current !in pathIdx) {
                pathIdx[current] = pathIdx.size  // 현재 노드와 경로상 위치 기록
                // choices는 1~n 범위의 학생 번호이므로, 배열 인덱스(0~n-1)로 변환하기 위해 -1
                current = choices[current] - 1
            }

            // 사이클 찾음: 현재 노드가 pathIdx에 있으면 사이클 발견
            if (current in pathIdx) {
                val cycleStartIdx = pathIdx[current]!!  // 사이클이 시작하는 위치
                val keys = pathIdx.keys.toList()        // 경로 상의 모든 노드 (순서대로)

                // cycleStartIdx 위치부터 끝까지가 사이클을 구성
                repeat(keys.size - cycleStartIdx) {
                    inCycle[keys[cycleStartIdx + it]] = true
                }

                // 경로의 모든 노드를 방문 처리
                repeat(keys.size) {
                    visited[keys[it]] = true
                }
            } else {
                // 사이클 없음: 경로 상의 모든 노드가 이미 처리된 경우
                // (visited[current]가 true인 경우로 사이클이 아님)
                pathIdx.keys.forEach { node ->
                    visited[node] = true
                }
            }
        }

        // 사이클에 속하지 않는 학생 수 계산
        println(n - inCycle.count { it })
    }
}
