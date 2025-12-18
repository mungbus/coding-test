package `251221`

fun main() {
    val n = readln().toLong()

    if (n < 2) {
        println(0)
        return
    }

    // 기본적으로 모든 쌍을 비교하는 횟수: n * (n - 1) / 2
    val base = n * (n - 1) / 2

    val result = if (n % 2 != 0L) {
        // n이 홀수일 때
        base
    } else {
        // n이 짝수일 때 (n-2) / 2 만큼 추가 연산 필요
        base + (n - 2) / 2
    }

    println(result)
}
