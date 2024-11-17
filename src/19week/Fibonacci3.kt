package `19week`

fun nthFibonacciMod(n: Long): Long {
    if (n == 0L) return 0
    if (n == 1L) return 1

    val mod = 1_000_000
    var a = 0L
    var b = 1L

    for (i in 2..n) {
        val temp = (a + b) % mod
        a = b
        b = temp
    }

    return b
}

fun main() {
    println(nthFibonacciMod(readln().toLong()))
}
