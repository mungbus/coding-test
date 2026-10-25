package `261025`

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer

class Territory {
    // 하드코딩된 매직 넘버 대신, 100번 초기화 시점의 (n + 최대 이주 횟수 1000)만큼만 딱 1번 할당!
    private var houses = LongArray(0)
    private var prefixSum = LongArray(0)
    private var size = 0
    private var offset = 0L

    // 100번: 게임 시작 (이 시점에 딱 필요한 만큼만 배열 1회 할당)
    fun init(n: Int, st: StringTokenizer): Territory {
        val maxCapacity = n + 1000 // 초기 집 n개 + 최대 이주 가능 횟수(1,000회)
        houses = LongArray(maxCapacity)
        prefixSum = LongArray(maxCapacity + 1)
        size = n
        offset = 0L

        repeat(n) { houses[it] = st.nextToken().toLong() }
        houses.sort(0, size) // 유효 범위(0 until size)만 정렬

        for (i in 0 until size) {
            prefixSum[i + 1] = prefixSum[i] + houses[i]
        }
        return this
    }

    // 200번: 이주 (새 배열을 할당하지 않고 기존 배열 내부에서 copyInto로 시프트 -> 추가 메모리 0)
    fun migrate(x: Long): Territory {
        val base = x - offset
        val idx = houses.binarySearch(base, 0, size).let { if (it < 0) -it - 1 else it }

        // 자기 자신(houses) 안에서 idx 뒤쪽 원소들을 오른쪽으로 한 칸 밀기
        houses.copyInto(houses, destinationOffset = idx + 1, startIndex = idx, endIndex = size)
        houses[idx] = base
        size++

        // 변경된 지점(idx)부터만 누적합 갱신
        for (i in idx until size) {
            prefixSum[i + 1] = prefixSum[i] + houses[i]
        }
        return this
    }

    // 300번: 영지 강화 (연산자 오버로딩)
    operator fun plus(p: Long): Territory {
        offset += p
        return this
    }

    // 400번: 겨울나기 (꼬리 재귀 이분 탐색)
    fun surviveWinter(p: Long): Long {
        fun neededFood(idx: Int): Long = (idx + 1).toLong() * houses[idx] - prefixSum[idx + 1]

        tailrec fun search(low: Int, high: Int, best: Int): Int {
            if (low > high) return best
            val mid = (low + high) ushr 1
            return if (neededFood(mid) <= p) {
                search(mid + 1, high, mid)
            } else {
                search(low, mid - 1, best)
            }
        }

        val idx = search(0, size - 1, 0)
        val remain = p - neededFood(idx)
        return houses[idx] + (remain / (idx + 1)) + offset
    }
}

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val q = br.readLine().trim().toInt()
    val out = StringBuilder()

    // 단일 Territory 인스턴스를 fold로 흘려보내 객체 생성 오버헤드(GC) 완벽 차단
    (1..q).fold(Territory()) { territory, _ ->
        val st = StringTokenizer(br.readLine())
        when (st.nextToken().toInt()) {
            100 -> territory.init(st.nextToken().toInt(), st)
            200 -> territory.migrate(st.nextToken().toLong())
            300 -> territory + st.nextToken().toLong()
            400 -> {
                out.appendLine(territory.surviveWinter(st.nextToken().toLong()))
                territory
            }

            else -> territory
        }
    }

    print(out)
}
