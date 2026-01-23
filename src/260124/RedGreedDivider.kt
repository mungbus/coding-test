package `260124`

val directions = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

fun countBothGroups(grid: Array<CharArray>, N: Int): Pair<Int, Int> {
    val visited = Array(N) { Array(N) { Pair(false, false) } }
    var normalCount = 0
    var colorBlindCount = 0

    repeat(N) { i ->
        repeat(N) { j ->
            val color = grid[i][j]
            val (normalVisited, colorBlindVisited) = visited[i][j]

            if (!normalVisited) {
                dfs(grid, visited, i, j, N, color, false)
                normalCount++
            }
            if (!colorBlindVisited) {
                dfs(grid, visited, i, j, N, color, true)
                colorBlindCount++
            }
        }
    }

    return Pair(normalCount, colorBlindCount)
}

fun dfs(
    grid: Array<CharArray>,
    visited: Array<Array<Pair<Boolean, Boolean>>>,
    x: Int,
    y: Int,
    N: Int,
    color: Char,
    isColorBlind: Boolean
) {
    // 경계 체크
    if (x !in 0..<N || y !in 0..<N) return

    // 방문 체크 (한 번만 접근)
    val (normalVisited, colorBlindVisited) = visited[x][y]

    if (isColorBlind) {
        if (colorBlindVisited) return
        val currentColor = grid[x][y]

        // 색상 비교
        if (!((color == 'R' || color == 'G') && (currentColor == 'R' || currentColor == 'G') || color == currentColor)) return

        // 방문 처리 (한 번만 접근)
        visited[x][y] = Pair(normalVisited, true)
    } else {
        if (normalVisited) return
        val currentColor = grid[x][y]

        // 색상 비교
        if (color != currentColor) return

        // 방문 처리 (한 번만 접근)
        visited[x][y] = Pair(true, colorBlindVisited)
    }
    // 상하좌우 탐색
    for ((dx, dy) in directions) {
        dfs(grid, visited, x + dx, y + dy, N, color, isColorBlind)
    }
}

fun main() {
    val N = readln().toInt()
    val grid = Array(N) { readln().toCharArray() }

    val (normal, colorBlind) = countBothGroups(grid, N)

    println("$normal $colorBlind")
}
