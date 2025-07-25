package `250525`

/*
각 우주별로 크기를 랭킹(동일값은 같은 랭크)으로 변환
랭킹 배열을 문자열(혹은 튜플)로 변환해 해시맵에 카운트
같은 랭킹 배열을 가진 우주 쌍의 수를 조합(pairCount)으로 합산
 */
fun getRanks(space: List<Int>): List<Int> {
    val sorted = space.withIndex().sortedBy { it.value }
    val ranks = IntArray(space.size)
    var rank = 0
    for (i in sorted.indices) {
        if (i > 0 && sorted[i].value != sorted[i - 1].value) rank++
        ranks[sorted[i].index] = rank
    }
    return ranks.toList()
}

fun pairCount(n: Int): Int = n * (n - 1) / 2

fun main() {
    val (M) = readln().split(" ").map { it.toInt() }
    val spaceCount = mutableMapOf<List<Int>, Int>()
    repeat(M) {
        val space = readln().split(" ").map { it.toInt() }
        val ranks = getRanks(space)
        spaceCount[ranks] = spaceCount.getOrDefault(ranks, 0) + 1
    }
    val result = spaceCount.values.filter { it > 1 }.sumOf { pairCount(it) }
    println(result)
}