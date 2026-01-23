package `260124`

/**
 * 로직 설명:
 * - 시각 1부터 T-1까지: 화력 감소 → 장작 넣기
 * - 시각 T: 도달 시점에 K개 이상이면 성공 (추가 화력 감소 없음)
 * - "놀이가 종료되기 전까지" = 시각 T 도달 전까지 K개 유지
 *
 * 타임라인 예시 (T=3):
 * 시각 0	1	                2	                3
 * 상태  [초기]  화력 감소 → 장작	화력 감소 → 장작	[K개 이상이면 성공]
 */
class AddingFirewood(val N: Int, val T: Int, val K: Int) {
    // 조건을 만족하는 경우의 수
    var count = 0

    /**
     * 1시간 동안의 모닥불 화력 감소를 시뮬레이션
     * @param fires 현재 모닥불의 화력 상태
     * @param skipIndex 장작을 넣어서 화력이 감소하지 않는 모닥불의 인덱스 (-1이면 모두 감소)
     * @return 화력 감소 후의 새로운 모닥불 상태
     */
    fun decreaseFires(fires: List<Int>, skipIndex: Int): List<Int> {
        // 원본을 수정하지 않도록 복사본 생성
        val newFires = fires.toMutableList()

        for (i in newFires.indices) {
            // 이미 꺼진 모닥불은 처리하지 않음 (다시 켜지지 않음)
            if (fires[i] <= 0) continue

            // 장작을 넣은 모닥불은 화력이 감소하지 않음
            if (i == skipIndex) continue

            // 인접한 켜진 모닥불 개수 세기 (화력 감소 전 상태 기준)
            var adjacentCount = 0
            if (i > 0 && fires[i - 1] > 0) adjacentCount++ // 왼쪽 모닥불
            if (i < N - 1 && fires[i + 1] > 0) adjacentCount++ // 오른쪽 모닥불

            // 인접한 켜진 모닥불 개수에 따라 화력 감소
            when (adjacentCount) {
                0 -> newFires[i] -= 3 // 인접한 모닥불 0개: -3
                1 -> newFires[i] -= 2 // 인접한 모닥불 1개: -2
                2 -> newFires[i] -= 1 // 인접한 모닥불 2개: -1
            }
        }

        return newFires
    }

    /**
     * 현재 켜져있는 모닥불의 개수를 계산
     * @param fires 모닥불의 화력 상태
     * @return 화력이 0보다 큰 모닥불의 개수
     */
    fun countActiveFires(fires: List<Int>): Int {
        return fires.count { it > 0 }
    }

    /**
     * DFS를 이용하여 모든 가능한 경로를 탐색
     * @param time 현재 시각 (1부터 시작)
     * @param pos SKH의 현재 위치 (모닥불 인덱스)
     * @param fires 현재 모닥불의 화력 상태
     */
    fun dfs(time: Int, pos: Int, fires: List<Int>) {
        // 가지치기: 현재 상태에서 K개 미만이면 실패이므로 더 이상 진행하지 않음
        if (countActiveFires(fires) < K) {
            return
        }

        // 종료 조건: 시각 T에 도달하면 성공 (화력 감소 전 시점에서 K개 이상 확인 완료)
        if (time == T) {
            count++
            return
        }

        // 각 시각마다의 진행 순서: 화력 감소 → 장작 넣기
        // decreaseFires의 skipIndex 매개변수로 장작을 넣을 위치를 제외하고 감소

        // 행동 1: 현재 위치에 머물며 장작 넣기
        val fires1 = decreaseFires(fires, pos)
        dfs(time + 1, pos, fires1)

        // 행동 2: 왼쪽으로 이동해서 장작 넣기
        if (pos > 0) {
            val fires2 = decreaseFires(fires, pos - 1)
            dfs(time + 1, pos - 1, fires2)
        }

        // 행동 3: 오른쪽으로 이동해서 장작 넣기
        if (pos < N - 1) {
            val fires3 = decreaseFires(fires, pos + 1)
            dfs(time + 1, pos + 1, fires3)
        }
    }
}

fun main() {
    // N: 모닥불 개수, W: SKH 시작 위치, T: 놀이 시간, K: 최소 유지 개수
    val (N, W, T, K) = readln().split(" ").map { it.toInt() }
    // fires: 각 모닥불의 초기 화력
    val fires = readln().split(" ").map { it.toInt() }
    val addingFirewood = AddingFirewood(N, T, K)

    // 초기 상태에서 K개 이상의 모닥불이 켜져있는지 확인
    if (addingFirewood.countActiveFires(fires) >= K) {
        // 시각 0 → 1로 넘어갈 때 화력 감소 적용 (-1 = 모든 모닥불 감소)
        val initialDecreased = addingFirewood.decreaseFires(fires, -1)

        // DFS 탐색 시작: 시각 1부터 (이미 화력 감소 적용된 상태)
        addingFirewood.dfs(1, W, initialDecreased)
    }

    // 조건을 만족하는 경우의 수 출력
    println(addingFirewood.count)
}
