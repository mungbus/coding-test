package `6week`

fun anthill(temp: List<List<String>>, depth: Int = 0) {
    if (temp.isEmpty()) return
    val result = temp.groupBy({
        if (it.isNotEmpty()) it.first()
        else return
    }, {
        if (it.isNotEmpty()) it.drop(1)
        else emptyList()
    })
    result.toSortedMap().forEach {
        println(Array(depth) { "--" }.joinToString("") + it.key)
        it.key to anthill(it.value, depth + 1)
    }
}

fun main() {
    val N = readln().toInt()
    var temp: List<List<String>> = Array(N) {
        readln().split(" ").drop(1)
    }.toList()
    anthill(temp)
}
