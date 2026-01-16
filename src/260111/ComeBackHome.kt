package `260111`

import kotlin.math.min
import kotlin.math.sqrt

fun main() {
    val (X, Y, D, T) = readln().split(" ").map { it.toInt() }

    val distance = sqrt(X * X + Y * Y + 0.0)

    // 경우 1: 모두 걷기
    var minTime = distance

    // 점프 가능 횟수 계산
    val jumpCount = (distance / D).toInt()

    // 경우 2: jumpCount번 점프 후 나머지 걷기
    if (jumpCount > 0) {
        val remaining = distance - jumpCount * D
        minTime = min(minTime, jumpCount * T + remaining)

        // 경우 3: (jumpCount + 1)번 점프 (원점을 넘어서 도착)
        minTime = min(minTime, (jumpCount + 1) * T.toDouble())
    }

    // 경우 4: jumpCount가 0일 때 (거리가 D보다 짧을 때)
    if (jumpCount == 0) {
        // 1번 점프 후 역방향으로 걷기
        minTime = min(minTime, T + D - distance)
        // 2번 점프
        minTime = min(minTime, 2 * T.toDouble())
    }

    println(minTime)
}
