package `18week`

fun main() {
    val (N, a, b) = readln().split(" ").map { it.toInt() }
    val MIN = 1

    val top = mutableListOf<Int>()

    if (a + b > N + MIN) {
        print(-1)
        return
    }

    for (i in MIN..<a) {
        top.add(i)
    }

    top.add(maxOf(a, b))

    for (i in b - MIN downTo MIN) {
        top.add(i)
    }

    while (top.size < N) {
        top.add(if (a == 1) 1 else 0, MIN)
    }
    println(top.joinToString(" "))
}
