package `250808`

fun main() {
    val (_, _, L, K) = readln().split(" ").map { it.toInt() }
    val meteors = mutableListOf<Pair<Int, Int>>()
    repeat(K) {
        val (x, y) = readln().split(" ").map { it.toInt() }
        meteors.add(x - 1 to y - 1)
    }

    var minMeteors = K

    for (x in meteors) {
        for (y in meteors) {
            val leftX = x.first
            val bottomY = y.second

            var count = 0
            for (meteor in meteors) {
                if (meteor.first < leftX || meteor.first > leftX + L ||
                    meteor.second < bottomY || meteor.second > bottomY + L
                ) {
                    count++
                }
            }
            minMeteors = minOf(minMeteors, count)
        }
    }

    println(minMeteors)
}
