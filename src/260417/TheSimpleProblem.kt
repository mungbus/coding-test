package `260417`

fun main() {
    // 1. 입력 받기
    val (n, p) = readln().split(" ").map { it.toInt() }

    // b배열의 크기를 딱 n으로 설정 (인덱스 0 ~ n-1 사용)
    val b = readln().split(" ").map { it.toInt() }

    // --- 검사 시작 ---

    // [필터 1] 첫 번째 기록 확인 (인덱스 0)
    if (b[0] != 1) {
        println("NO")
        return
    }

    // [필터 2] 루프를 돌며 연속성과 하한선 확인
    for (i in 0 until n) {
        // (1) 이전 기록과 비교 (인덱스 1부터 수행)
        if (i > 0) {
            // b[i]는 현재 기록, b[i-1]은 바로 직전 기록
            if (b[i] < b[i - 1] || b[i] > b[i - 1] + 1) {
                println("NO")
                return
            }
        }

        // (2) 비둘기집 원리 확인
        // i번째 공은 인덱스 i에 해당하므로, 실제 공의 개수는 (i + 1)개임
        val currentBallCount = i + 1
        if (b[i].toLong() * p < currentBallCount) {
            println("NO")
            return
        }
    }

    // [필터 3] 최종 상태 확인 (인덱스 n-1이 마지막 기록)
    val base = n / p
    val remainder = n % p
    val maxPossibleCount = if (remainder > 0) base + 1 else base

    // 마지막 기록 b[n-1]이 실제 가능한 높이와 맞는지 확인
    if (b[n - 1] < maxPossibleCount) {
        println("NO")
        return
    }

    println("YES")
}
