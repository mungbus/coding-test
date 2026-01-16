package `260111`

import kotlin.math.min
import kotlin.math.sqrt

/**
 * 문제: 원점(0,0)에서 목적지(X,Y)까지 최소 시간으로 이동하기
 *
 * 이동 방법:
 * 1. 걷기: 단위 거리당 1의 시간 소요 (어느 방향이든 자유롭게)
 * 2. 점프: 거리 D를 시간 T에 이동 (어느 방향이든 가능)
 *
 * 풀이 전략:
 * - 점프는 어느 방향으로든 가능하므로, 목적지 방향으로 점프한 뒤 조정 가능
 * - 여러 경우를 비교하여 최소값을 찾는다
 *
 * 주의사항:
 * - X, Y가 최대 10^9이므로 X*X + Y*Y 계산 시 Long 타입 필요 (오버플로우 방지)
 */
fun main() {
    // 입력: X, Y(목적지 좌표), D(점프 거리), T(점프 시간)
    val (X, Y, D, T) = readln().split(" ").map { it.toLong() }

    // 1단계: 원점에서 목적지까지의 유클리드 거리 계산
    // distance = √(X² + Y²)
    val distance = sqrt(X * X + Y * Y + 0.0)

    // 2단계: 경우 1 - 모두 걷기 (기본 경우)
    // 직선으로 걸어가면 거리 = 시간
    var minTime = distance

    // 3단계: 목적지 방향으로 몇 번 점프 가능한지 계산
    // jumpCount = ⌊distance / D⌋
    val jumpCount = (distance / D).toInt()

    // 4단계: jumpCount > 0인 경우 (점프를 최소 1번 이상 할 수 있는 경우)
    if (jumpCount > 0) {
        // 경우 2: jumpCount번 점프 + 나머지 거리는 걷기
        // 목적지 방향으로 최대한 점프한 후, 남은 거리를 걸어서 정확히 도착
        val remaining = distance - jumpCount * D
        minTime = min(minTime, jumpCount * T + remaining)

        // 경우 3: (jumpCount + 1)번 점프
        // 목적지를 넘어서 점프한 후, 역방향으로 한 번 더 점프하여 근처에 도착
        // 남은 거리가 길면 이 방법이 더 빠를 수 있음 (T < D인 경우)
        minTime = min(minTime, (jumpCount + 1) * T.toDouble())
    }

    // 5단계: jumpCount == 0인 경우 (거리가 D보다 짧을 때)
    if (jumpCount == 0) {
        // 경우 4-1: 1번 점프 후 역방향으로 걷기
        // 목적지 방향으로 D만큼 점프 → (D - distance)만큼 뒤로 걸어오기
        // 총 시간: T + (D - distance)
        minTime = min(minTime, T + D - distance)

        // 경우 4-2: 2번 점프
        // 첫 번째 점프로 목적지를 넘어가고, 두 번째 점프로 근처에 도착
        // 점프 시간이 매우 짧으면 (T << D) 이 방법이 유리할 수 있음
        minTime = min(minTime, 2 * T.toDouble())
    }

    // 6단계: 모든 경우 중 최소 시간 출력
    println(minTime)
}
