import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.PriorityQueue
import java.util.StringTokenizer

// 1. 선박 객체 정의
data class Ship(
    val id: Int,
    var p: Int,
    val r: Int,
    var isReloading: Boolean = false
) {
    // PriorityQueue에 넣을 독립적인 스냅샷 객체 생성 메서드
    fun toQueueState() = Ship(id, p, r)
}

// 2. 핵심 자료구조 전역 선언 (함수 간 공유를 위해 main 밖으로 이동)
val shipMap = HashMap<Int, Ship>()
val pq = PriorityQueue<Ship> { a, b ->
    if (a.p != b.p) b.p.compareTo(a.p) // 공격력 큰 순 (내림차순)
    else a.id.compareTo(b.id)          // ID 작은 순 (오름차순)
}
val reloadMap = HashMap<Int, MutableList<Int>>()

// 입력 스트림을 받아 선박을 생성하고 등록하는 공통 함수
fun parseAndAddShip(st: StringTokenizer) {
    val id = st.nextToken().toInt()
    val p = st.nextToken().toInt()
    val r = st.nextToken().toInt()

    val ship = Ship(id, p, r)
    shipMap[id] = ship
    pq.add(ship.toQueueState())
}

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    var st: StringTokenizer

    val T = br.readLine().toInt()
    val sb = StringBuilder()
    var currentTime = 1

    repeat(T) {
        st = StringTokenizer(br.readLine())
        val cmd = st.nextToken().toInt()

        // [단계 0] 현재 턴에 재장전이 완료된 선박들을 복귀
        reloadMap[currentTime]?.forEach { shipId ->
            shipMap[shipId]?.let { ship ->
                ship.isReloading = false
                pq.add(ship.toQueueState()) // 부활 시점의 최신 공격력 반영
            }
            reloadMap.remove(currentTime)
        }

        // [단계 1] 명령어별 처리
        when (cmd) {
            100 -> { // 공격 준비
                val n = st.nextToken().toInt()
                repeat(n) {
                    parseAndAddShip(st)
                }
            }

            200 -> parseAndAddShip(st) // 지원 요청

            300 -> { // 함포 교체
                val id = st.nextToken().toInt()
                val pw = st.nextToken().toInt()

                shipMap[id]?.let { ship ->
                    ship.p = pw

                    // 사격 대기 상태인 선박만 새 정보를 PQ에 미리 push (Lazy Deletion 준비)
                    if (!ship.isReloading) {
                        pq.add(ship.toQueueState())
                    }
                }
            }

            400 -> { // 공격 명령
                val selected = mutableListOf<Ship>()

                while (pq.isNotEmpty() && selected.size < 5) {
                    val top = pq.poll()
                    val actualShip = shipMap[top.id]!!

                    // Lazy Deletion 검증
                    if (top.p != actualShip.p || actualShip.isReloading) {
                        continue
                    }

                    selected.add(actualShip)
                }

                var totalDamage = 0L
                val shipCount = selected.size

                for (ship in selected) {
                    totalDamage += ship.p
                    ship.isReloading = true

                    val wakeUpTime = currentTime + ship.r
                    reloadMap.computeIfAbsent(wakeUpTime) { mutableListOf() }.add(ship.id)
                }

                // 출력 포맷 생성
                sb.append(totalDamage).append(" ").append(shipCount)
                for (ship in selected) {
                    sb.append(" ").append(ship.id)
                }
                sb.append("\n")
            }
        }
        currentTime++
    }
    print(sb)
}
