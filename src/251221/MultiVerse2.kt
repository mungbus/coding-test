package `251221`

/**
 * 행성들의 크기를 입력받아 상대적인 순위(Rank) 리스트로 변환하는 함수
 * 문제의 조건 (Ai < Aj -> Bi < Bj 등)을 만족하는지 확인하기 위해 좌표 압축을 수행합니다.
 */
fun getRanks(space: List<Int>): List<Int> {
    // 1. (인덱스, 값) 쌍으로 만든 뒤 값을 기준으로 오름차순 정렬합니다.
    // 예: [20, 10, 20] -> [(0, 20), (1, 10), (2, 20)] -> 정렬 후 [(1, 10), (0, 20), (2, 20)]
    val sorted = space.withIndex().sortedBy { it.value }

    val ranks = IntArray(space.size)
    var rank = 0

    for (i in sorted.indices) {
        // 2. 이전 값과 현재 값이 다르다면 순위(rank)를 1 증가시킵니다.
        // 값이 같다면(Ai = Aj) 같은 순위를 부여하게 됩니다.
        if (i > 0 && sorted[i].value != sorted[i - 1].value) {
            rank++
        }
        // 3. 원래 위치(index)에 계산된 순위를 저장합니다.
        ranks[sorted[i].index] = rank
    }

    // 4. Map의 키로 사용하기 위해 리스트 형태로 반환합니다.
    return ranks.toList()
}

/**
 * n개의 동일한 구성을 가진 우주 중에서 2개를 뽑는 조합(nC2)의 수를 계산합니다.
 */
fun pairCount(n: Int): Int = n * (n - 1) / 2

fun main() {
    // M: 우주의 개수, N: 행성의 개수
    val (M, N) = readln().split(" ").map { it.toInt() }

    // 순위 리스트(List<Int>)를 키로 하여 해당 구성을 가진 우주의 개수를 카운트합니다.
    val spaceCount = mutableMapOf<List<Int>, Int>()

    repeat(M) {
        // 각 우주의 행성 크기들을 입력받아 순위 리스트로 변환 후 맵에 기록합니다.
        val space = readln().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val ranks = getRanks(space)
        spaceCount[ranks] = spaceCount.getOrDefault(ranks, 0) + 1
    }

    // 1개보다 많이 존재하는(중복된 구성) 우주들에 대해 만들 수 있는 쌍의 합을 구합니다.
    val result = spaceCount.values.sumOf { count ->
        if (count > 1) pairCount(count) else 0
    }

    println(result)
}
