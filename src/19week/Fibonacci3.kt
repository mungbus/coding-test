package `19week`

private const val mod = 1_000_000

fun nthFibonacciMod(n: Long): Int {
    if (n == 0L) return 0
    if (n == 1L) return 1

    val baseMatrix = arrayOf(
        longArrayOf(1, 1),
        longArrayOf(1, 0)
    )

    val resultMatrix = baseMatrix.matrixPower(n - 1)
    return (resultMatrix[0][0] % mod).toInt()
}

fun Array<LongArray>.matrixPower(power: Long): Array<LongArray> {
    var result = Array(size) {
        LongArray(size) {
            if (it == 0) 1L else 0L
        }
    }
    var base = copyOf()
    var exp = power

    while (exp > 0) {
        if (exp % 2 == 1L) {
            result = result.matrixMultiply(base)
        }
        base = base.matrixMultiply(base)
        exp /= 2
    }
    return result
}

fun Array<LongArray>.matrixMultiply(other: Array<LongArray>): Array<LongArray> {
    val result = Array(size) { LongArray(size) }
    for (i in indices) {
        for (j in indices) {
            for (k in indices) {
                result[i][j] = (result[i][j] + this[i][k] * other[k][j]) % mod
            }
        }
    }
    return result
}

fun main() {
    println(nthFibonacciMod(readln().toLong()))
}
