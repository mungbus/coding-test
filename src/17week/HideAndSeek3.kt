package `17week`

val visited = mutableSetOf<Int>()
var answer = -1
const val MAX = 100_000

fun next(current: Int, second: Int = 0): Collection<Pair<Int, Int>> {
    visited.add(current)
    val next = mutableListOf<Pair<Int, Int>>()
    if (current % 2 == 0 && !visited.contains(current / 2)) {
        var temp = current
        while (temp % 2 == 0 && temp >= 0) {
            temp /= 2
            next.add(temp to second)
        }
    }
    if (!visited.contains(current + 1) && current + 1 <= MAX) {
        next.add(current + 1 to second + 1)
    }
    if (current >= 1 && !visited.contains(current - 1)) {
        next.add(current - 1 to second + 1)
    }
    return next
}

fun main() {
    val (N, K) = readln().split(" ").map { it.toInt() }
    try {
        recursive(N, K)
    } catch (_: Exception) {
    }
    println(answer)
}

fun recursive(current: Int, end: Int, second: Int = 0) {
    val list = next(end, second)
    list.forEach { (next, second) ->
        if (next == current) {
            if (answer == -1 || answer > second) {
                answer = second
            }
        }
    }
    if (answer != -1) {
        throw RuntimeException()
    }
    list.forEach { (next, second) ->
        recursive(current, next, second)
    }
}
