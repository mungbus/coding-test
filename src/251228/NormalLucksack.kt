package `251228`

fun main() {
    val (n, k) = readln().split(" ").map { it.toInt() }

    // 각 물건의 무게와 가치를 저장
    val items = Array(n) { Pair(0, 0) }
    repeat(n) { i ->
        val (w, v) = readln().split(" ").map { it.toInt() }
        items[i] = Pair(w, v)
    }

    // dp[i] = 용량 i일 때 얻을 수 있는 최대 가치
    val dp = IntArray(k + 1)

    // 각 물건을 순회
    repeat(n) { i ->
        val (weight, value) = items[i]

        // 뒤에서 앞으로 순회 (같은 물건을 중복으로 선택하지 않기 위함)
        for (w in k downTo weight) {
            dp[w] = maxOf(dp[w], dp[w - weight] + value)
        }
    }

    println(dp[k])
}
