package `250913`

fun isPossible(homeList: Array<Int>, C: Int, distance: Int): Boolean {
    var count = 1
    var lastPosition = homeList[0]

    for (i in 1..<homeList.size) {
        if (homeList[i] - lastPosition >= distance) {
            count++
            lastPosition = homeList[i]
            if (count >= C) {
                return true
            }
        }
    }
    return false
}

fun main() {
    val (N, C) = readln().split(" ").map { it.toInt() }
    val homeList = Array(N) {
        readln().toInt()
    }.apply { sort() }

    var start = 1
    var end = homeList.last() - homeList.first()
    var result = 0

    while (start <= end) {
        val mid = start + (end - start) / 2
        if (isPossible(homeList, C, mid)) {
            result = mid
            start = mid + 1
        } else {
            end = mid - 1
        }
    }
    println(result)
}
