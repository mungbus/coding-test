package `260627`

// 1. Pair 연산자 오버로딩 (좌표 덧셈)
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + other.first, this.second + other.second)
}

// 4방향 벡터
val directions = listOf(
    Pair(-1, 0), // 상
    Pair(1, 0), // 하
    Pair(0, -1), // 좌
    Pair(0, 1)  // 우
)

var answer = 0
var n = 0
var m = 0
lateinit var grid: Array<IntArray>
lateinit var visited: Array<BooleanArray>
lateinit var targets: List<Pair<Int, Int>>

fun dfs(pos: Pair<Int, Int>, targetIdx: Int) {
    // 1. 현재 칸이 목표한 중간 목적지인지 확인 후 인덱스 갱신
    val nextTargetIdx = if (pos == targets[targetIdx]) targetIdx + 1 else targetIdx

    // 2. 종료 조건: 최종 목적지에 도달한 경우
    if (pos == targets[m - 1]) {
        // 모든 중간 목적지를 순서대로 밟고 왔는지 검증 (새치기 방지)
        if (nextTargetIdx == m) {
            answer++
        }
        return
    }

    // 3. 4방향 탐색 및 백트래킹
    directions.forEach { dir ->
        val nextPos = pos + dir
        val (nx, ny) = nextPos

        if (nx in 0 until n && ny in 0 until n && grid[nx][ny] == 0 && !visited[nx][ny]) {
            visited[nx][ny] = true
            dfs(nextPos, nextTargetIdx)
            visited[nx][ny] = false
        }
    }
}

fun main() {
    // 1. n, m 입력 (readln 사용)
    val nm = readln().split(" ").map { it.toInt() }
    n = nm[0]
    m = nm[1]

    // 2. 격자 입력 (repeat 대신 Array 초기화 블록을 쓰면 한 줄로 끝납니다!)
    grid = Array(n) { readln().split(" ").map { it.toInt() }.toIntArray() }

    // 3. 목적지 입력 (List 초기화 블록 사용, 0-based 인덱스 보정)
    targets = List(m) {
        val (x, y) = readln().split(" ").map { it.toInt() }
        Pair(x - 1, y - 1)
    }

    visited = Array(n) { BooleanArray(n) }

    // 시작점 방문 처리 및 DFS 시작
    val startPos = targets[0]
    visited[startPos.first][startPos.second] = true

    dfs(startPos, 0)

    println(answer)
}
