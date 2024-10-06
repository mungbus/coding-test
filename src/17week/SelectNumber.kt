package `17week`

fun Collection<Pair<Int, Int>>.check(): Boolean {
    val firstSet = mutableSetOf<Int>()
    val secondSet = mutableSetOf<Int>()
    forEach {
        firstSet.add(it.first)
        secondSet.add(it.second)
    }
    return secondSet.containsAll(firstSet)
}

fun main() {
    val N = readln().toInt()
    val numberList = Array(N) {
        val index = it + 1
        val value = readln().toInt()
        index to value
    }.toList()
    (N downTo 1).forEach { n ->
        numberList.combinations(n).forEach {
            if (it.check()) {
                println("$n\n${it.map { it.first }.joinToString("\n")}")
                return
            }
        }
    }
}

fun <T> Collection<T>.combinations(n: Int): List<List<T>> {
    if (n == size) return listOf(toList())
    if (n == 0 || n > size) return emptyList()
    if (n == 1) return map { listOf(it) }
    val list = toList()
    return list.flatMapIndexed { i, element ->
        val subList = list.subList(i + 1, list.size).combinations(n - 1)
        subList.map { sub ->
            listOf(element) + sub
        }
    }
}
