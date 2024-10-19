package `18week`

fun maxHamburgers(table: String, k: Int): Int {
    val people = mutableListOf<Int>()
    val hamburgers = mutableListOf<Int>()
    for (i in table.indices) {
        if (table[i] == 'P') {
            people.add(i)
        } else if (table[i] == 'H') {
            hamburgers.add(i)
        }
    }

    var pIndex = 0
    var hIndex = 0
    var count = 0

    while (pIndex < people.size && hIndex < hamburgers.size) {
        if (Math.abs(people[pIndex] - hamburgers[hIndex]) <= k) {
            count++
            pIndex++
            hIndex++
        } else {
            if (people[pIndex] < hamburgers[hIndex]) {
                pIndex++
            } else {
                hIndex++
            }
        }
    }

    return count
}

fun main() {
    val (_, K) = readln().split(" ").map { it.toInt() }
    val table = readln()
    println(maxHamburgers(table, K))
}
