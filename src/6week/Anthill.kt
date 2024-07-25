package `6week`

fun List<List<String>>.anthill(depth: Int = 0) {
    groupBy({
        if (it.isEmpty()) return
        it.first()
    }, {
        if (it.isEmpty()) return
        it.drop(1)
    }).toSortedMap().forEach {
        println("--".repeat(depth) + it.key)
        it.key to it.value.anthill(depth + 1)
    }
}

fun main() {
    val N = readln().toInt()
    Array(N) {
        readln().split(" ").drop(1)
    }.toList().anthill()
}
