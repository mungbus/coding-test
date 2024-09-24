package `14week`

fun main() {
    val (N, d, k, c) = readln().split(" ").map { it.toInt() }
    val sushiList = Array(N) {
        readln().toInt()
    }

    val sushiCount = IntArray(d + 1)
    var answer: Int
    var kinds = 0

    Array(k) {
        if (sushiCount[sushiList[it]] == 0) {
            kinds++
        }
        sushiCount[sushiList[it]]++
    }

    answer = kinds + if (sushiCount[c] == 0) 1 else 0

    (0..<N - 1).forEach {
        val leavingSushi = sushiList[it]
        sushiCount[leavingSushi]--
        if (sushiCount[leavingSushi] == 0) {
            kinds--
        }
    }
    repeat(N) {
        val removeIndex = sushiList[it]
        sushiCount[removeIndex]--
        if (sushiCount[removeIndex] == 0) {
            kinds--
        }

        val newSushi = sushiList[(it + k) % N]
        if (sushiCount[newSushi] == 0) {
            kinds++
        }
        sushiCount[newSushi]++
        answer = maxOf(answer, kinds + if (sushiCount[c] == 0) 1 else 0)
    }
    println(answer)
}
