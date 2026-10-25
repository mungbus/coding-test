package `261025`

import java.util.StringTokenizer

/**
 * 집들의 '기준 식량 비축량(base)' 목록과 정렬/누적합 캐시를 관리하는 불변(Immutable) 스냅샷 클래스
 * - 100번(초기화)과 200번(이주) 이벤트가 발생할 때만 새 인스턴스가 생성됩니다.
 * - 300번(영지 강화) 이벤트가 발생해도 집들의 상대적 차이는 변하지 않으므로 이 스냅샷을 그대로 재사용합니다.
 */
class HouseSnapshot(val houses: List<Long> = emptyList()) {

    /**
     * [Lazy Evaluation 최적화]
     * 200번(이주) 이벤트가 연속으로 들어올 때마다 매번 정렬하면 비효율적입니다.
     * 따라서 실제로 400번(겨울나기) 이벤트가 발생해 `prepared` 프로퍼티를 읽는 순간에만
     * 딱 한 번 정렬(sorted)과 누적합(runningFold)을 계산하고 결과를 캐싱해 둡니다.
     *
     * - LazyThreadSafetyMode.NONE: 단일 스레드 환경이므로 불필요한 동기화(Lock) 오버헤드를 제거합니다.
     */
    val prepared: Pair<List<Long>, List<Long>> by lazy(LazyThreadSafetyMode.NONE) {
        // 1. 기준 식량 비축량(base)을 오름차순으로 정렬
        val sorted = houses.sorted()

        // 2. 빠른 구간 합 계산을 위한 누적합(Prefix Sum) 리스트 생성
        // 예: sorted가 [1, 3, 5]라면 -> prefixSum은 [0, 1, 4, 9] (크기: N + 1)
        // prefixSum[k]는 가장 적은 집부터 k개의 집이 가진 식량의 총합을 의미합니다.
        val prefixSum = sorted.runningFold(0L, Long::plus)

        sorted to prefixSum
    }

    /**
     * 연산자 오버로딩 (+): 새로운 집(base)이 추가된 새 스냅샷을 반환합니다.
     * 새 인스턴스가 만들어지므로 다음 400번 이벤트 호출 시 자연스럽게 새로 정렬됩니다.
     */
    operator fun plus(base: Long) = HouseSnapshot(houses + base)
}

/**
 * 영지의 전체 상태를 나타내는 불변 클래스
 * @property snapshot 현재 집들의 기준값(base) 스냅샷
 * @property offset 지금까지 누적된 '영지 강화(300번)'의 총합
 */
class Territory(
    private val snapshot: HouseSnapshot = HouseSnapshot(),
    private val offset: Long = 0L
) {
    /**
     * [100번: 게임 시작]
     * 초기 집들의 리스트로 스냅샷을 생성하고, 누적 강화량(offset)을 0으로 초기화합니다.
     */
    fun init(houses: List<Long>) = Territory(HouseSnapshot(houses), 0L)

    /**
     * [200번: 이주]
     * 새로 들어온 집(비축량 x)은 이전까지의 영지 강화(offset) 효과를 받지 않습니다.
     * 하지만 추후 모든 집을 계산할 때 일괄적으로 `+ offset`을 해줄 것이므로,
     * 미리 `x - offset`을 기준값(base)으로 저장해 두면 수식이 완벽하게 맞아떨어집니다.
     * -> (x - offset) + 현재_offset = x
     */
    fun migrate(x: Long) = Territory(snapshot + (x - offset), offset)

    /**
     * [300번: 영지 강화]
     * 모든 집에 p를 직접 더하는 대신 offset만 p만큼 증가시킵니다. (O(1) 시간 복잡도)
     * 중요: 기존 `snapshot` 인스턴스를 그대로 넘기므로, 이미 계산된 정렬/누적합 캐시(by lazy)가 깨지지 않고 유지됩니다!
     */
    operator fun plus(p: Long) = Territory(snapshot, offset + p)

    /**
     * [400번: 겨울나기]
     * 식량 p를 분배해 만들 수 있는 '가장 적은 식량 비축량의 최댓값'을 구합니다.
     */
    fun surviveWinter(p: Long): Long {
        // 이 시점에 최초로 정렬 및 누적합 계산이 수행되거나, 이미 캐싱된 데이터를 O(1)에 가져옵니다.
        val (houses, prefixSum) = snapshot.prepared

        /**
         * 0번 집부터 idx번 집(총 idx + 1개)의 식량을 모두 `houses[idx]` 높이로 똑같이 맞추기 위해
         * 추가로 보급해야 하는 식량의 총량을 O(1)에 계산합니다.
         *
         * 수식: (맞추려는 집의 개수 * 목표 높이) - (해당 집들이 원래 가지고 있던 식량의 총합)
         */
        fun neededFood(idx: Int): Long = (idx + 1).toLong() * houses[idx] - prefixSum[idx + 1]

        /**
         * 꼬리 재귀(tailrec)를 이용한 이분 탐색
         * 주어진 식량 p로 높이를 평탄화할 수 있는 '가장 오른쪽 집의 인덱스(best)'를 찾습니다.
         */
        tailrec fun search(low: Int, high: Int, best: Int): Int {
            if (low > high) return best
            val mid = (low + high) ushr 1 // 오버플로우 없는 안전한 중간값 계산 ((low + high) / 2)

            return if (neededFood(mid) <= p) {
                // mid번 집 높이까지 맞추고도 식량이 남거나 딱 맞다면 -> 더 높은 집까지 맞출 수 있는지 오른쪽 탐색
                search(mid + 1, high, mid)
            } else {
                // 식량이 부족하다면 -> 범위를 왼쪽으로 좁혀 탐색
                search(low, mid - 1, best)
            }
        }

        // 1. 주어진 식량 p로 커버 가능한 최대 집의 인덱스(idx) 도출 (시간 복잡도: O(log N))
        val idx = search(0, houses.lastIndex, 0)

        // 2. 0번 ~ idx번 집(총 idx + 1개)을 모두 houses[idx] 높이로 맞춘 뒤 남은 잉여 식량 계산
        val remain = p - neededFood(idx)

        // 3. 남은 식량을 (idx + 1)개의 집에 공평하게 1/N로 나누어 줌 (정수 나눗셈의 몫만큼 추가 상승)
        val maxMinBase = houses[idx] + (remain / (idx + 1))

        // 4. 기준값(base)에 그동안 누적된 영지 강화량(offset)을 더해 실제 식량 비축량 반환
        return maxMinBase + offset
    }
}

fun main() {
    val q = readln().trim().toInt()
    val out = StringBuilder()

    // Q개의 이벤트를 순회하며 초기 상태(Territory())에서 다음 상태로 끊임없이 전이(fold)시킴
    (1..q).fold(Territory()) { territory, _ ->
        val st = StringTokenizer(readln())
        when (st.nextToken().toInt()) {
            100 -> {
                val n = st.nextToken().toInt()
                territory.init(List(n) { st.nextToken().toLong() })
            }

            200 -> territory.migrate(st.nextToken().toLong())
            300 -> territory + st.nextToken().toLong()
            400 -> {
                out.appendLine(territory.surviveWinter(st.nextToken().toLong()))
                territory // 400번은 일시적 보급이므로 상태(territory)를 변경하지 않고 그대로 다음 턴에 넘김
            }

            else -> territory
        }
    }

    print(out)
}
