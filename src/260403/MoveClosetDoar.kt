package `260403`

import kotlin.math.abs
import kotlin.math.min

lateinit var memo: Array<Array<IntArray>>
lateinit var targets: IntArray
var totalSteps: Int = 0
var n: Int = 0

fun main() {
    // 1. 벽장의 개수
    n = readln().toInt()

    // 2. 초기 열린 벽장 위치
    val (open1, open2) = readln().split(" ").map { it.toInt() }

    // 3. 사용할 벽장 순서의 길이
    totalSteps = readln().toInt()

    // 4. 사용할 벽장 번호들 입력
    targets = Array(totalSteps) {
        readln().toInt()
    }.toIntArray()

    // DP 테이블 초기화
    memo = Array(totalSteps) { Array(n + 1) { IntArray(n + 1) { -1 } } }

    println(recursive(0, open1, open2))
}

/**
 * DP(idx, o1, o2)를 'idx번째 벽장을 사용할 차례이고 현재 o1, o2가 열려있을 때의 최소 이동 횟수'라고 정의함.
 *
 * 1. o1을 목적지(target)로 이동시키는 경우:
 * 비용 = |o1 - target| + DP(idx + 1, target, o2)
 * (o1이 target으로 갔으므로 다음 상태에서 열린 곳은 target과 o2가 됨)
 *
 * 2. o2를 목적지(target)로 이동시키는 경우:
 * 비용 = |o2 - target| + DP(idx + 1, o1, target)
 * (o2가 target으로 갔으므로 다음 상태에서 열린 곳은 o1과 target이 됨)
 *
 * 최종:
 * DP(idx, o1, o2) = min( (abs(o1-target) + DP(idx+1, target, o2)), (abs(o2-target) + DP(idx+1, o1, target)) )
 */
fun recursive(idx: Int, o1: Int, o2: Int): Int {
    // 모든 요청을 처리한 경우
    if (idx == totalSteps) return 0
    // 이미 계산된 값이 있는 경우 결과 즉시 반환
    if (memo[idx][o1][o2] != -1) return memo[idx][o1][o2]

    val target = targets[idx]

    // 왼쪽 문(o1)을 옮길 때의 최소값 계산
    val moveOpen1 = abs(o1 - target) + recursive(idx + 1, target, o2)
    // 오른쪽 문(o2)을 옮길 때의 최소값 계산
    val moveOpen2 = abs(o2 - target) + recursive(idx + 1, o1, target)

    // 두 가지 선택지 중 최솟값을 기록 후 반환
    memo[idx][o1][o2] = min(moveOpen1, moveOpen2)
    return memo[idx][o1][o2]
}
