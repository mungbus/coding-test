package `11week`

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val (N, M) = scanner.nextLine().split(" ").map { it.toInt() }
    val groundHeight = Array(N) {
        scanner.nextInt()
    }
    scanner.nextLine()
    val sumHeight = Array(N + 1) { 0 }
    repeat(M) {
        val (a, b, k) = scanner.nextLine().split(" ").map { it.toInt() }
        sumHeight[a - 1] += k
        sumHeight[b] -= k
    }
    repeat(N) {
        sumHeight[it + 1] += sumHeight[it]
        print(groundHeight[it] + sumHeight[it])
        if (it != N - 1) print(" ")
    }
}
