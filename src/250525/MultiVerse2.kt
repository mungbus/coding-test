package `250525`

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