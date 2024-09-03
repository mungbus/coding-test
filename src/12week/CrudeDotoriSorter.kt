package `12week`

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val N = scanner.nextInt()

    val hole = Array(N) {
        Pair(scanner.nextInt() + it, it + 1)
    }

    val M = scanner.nextInt()
    val dotori = Array(M) {
        scanner.nextInt()
    }

    val sorter = IntArray(N + 1)
    var end = 0
    for (i in 0..<N) {
        if (hole[i].first <= end) continue
        if (sorter[N] != 0) break
        for (j in end..minOf(hole[i].first, N)) {
            if (sorter[j] == 0) {
                sorter[j] = hole[i].second
            }
        }
        end = hole[i].first
    }

    val answer = dotori.map {
        sorter[it]
    }.joinToString(" ")
    println(answer)
}
