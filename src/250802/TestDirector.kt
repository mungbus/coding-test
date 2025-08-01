package `250802`

fun main() {
    readln()
    val testN = readln().split(" ").map { it.toInt() }
    val (B, C) = readln().split(" ").map { it.toInt() }
    var total = 0L
    testN.forEach { test ->
        val remain = test - B
        total += 1 + (if (remain > 0) (remain + C - 1) / C else 0)
    }

    println(total)
}
