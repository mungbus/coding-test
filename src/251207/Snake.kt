package `251207`

fun main() {
    val N = readln().toInt()
    val K = readln().toInt()

    // 사과 위치 저장 (1-indexed)
    val apples = Array(K) {
        val (r, c) = readln().split(" ").map { it.toInt() }
        Pair(r, c)
    }.toMutableSet()

    val L = readln().toInt()
    // 방향 변경 정보 저장 (Map으로 시간별 접근 최적화)
    val directionChanges = (1..L).map {
        val (xStr, cStr) = readln().split(" ")
        Pair(xStr.toInt(), cStr[0])
    }.associate { it.first to it.second }

    // 방향 0=오른쪽, 1=아래, 2=왼쪽, 3=위
    val directions = listOf(
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1),
        Pair(-1, 0)
    )

    // 뱀의 몸을 저장 (front가 머리)
    val snake = mutableListOf(Pair(1, 1)) // 1-indexed에서 (1, 1)부터 시작

    // 게임 실행
    val result = playGame(N, snake, apples, directions, directionChanges, 0, 0)
    println(result)
}

fun playGame(
    N: Int,
    snake: MutableList<Pair<Int, Int>>,
    apples: MutableSet<Pair<Int, Int>>,
    directions: List<Pair<Int, Int>>,
    directionChanges: Map<Int, Char>,
    time: Int,
    direction: Int
): Int {
    val nextTime = time + 1

    // 현재 시간의 방향으로 이동 (이전 방향)
    val head = snake.last()
    val (dr, dc) = directions[direction]
    val newHeadR = head.first + dr
    val newHeadC = head.second + dc

    // 벽과 충돌 확인
    if (newHeadR !in 1..N || newHeadC !in 1..N) {
        return nextTime
    }

    // 자신의 몸과 충돌 확인
    if (snake.contains(Pair(newHeadR, newHeadC))) {
        return nextTime
    }

    // 새로운 머리 추가
    snake.add(Pair(newHeadR, newHeadC))

    // 사과 여부 확인 및 꼬리 처리
    val newApples = if (apples.contains(Pair(newHeadR, newHeadC))) {
        apples.remove(Pair(newHeadR, newHeadC))
        apples // 사과가 있으면 꼬리 유지 (길이 증가)
    } else {
        snake.removeAt(0) // 사과가 없으면 꼬리 제거 (길이 유지)
        apples
    }

    // nextTime 이후의 방향 변경 확인 (nextTime초가 끝난 뒤)
    val nextDirection = directionChanges[nextTime]?.let { turn ->
        if (turn == 'L') {
            (direction + 3) % 4 // 시계 반대 방향
        } else {
            (direction + 1) % 4 // 시계 방향
        }
    } ?: direction

    return playGame(N, snake, newApples, directions, directionChanges, nextTime, nextDirection)
}
