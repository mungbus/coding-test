package `260808`

import java.util.Scanner

data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun times(scale: Int): Pos = Pos(x * scale, y * scale)
    fun inBounds(): Boolean = x in 0..3 && y in 0..3
}

data class Fish(var num: Int, var dir: Int)

// 1~8번 방향 (0번은 dummy)
val directions = arrayOf(
    Pos(0, 0),
    Pos(-1, 0),  // 1: ↑
    Pos(-1, -1), // 2: ↖
    Pos(0, -1),  // 3: ←
    Pos(1, -1),  // 4: ↙
    Pos(1, 0),   // 5: ↓
    Pos(1, 1),   // 6: ↘
    Pos(0, 1),   // 7: →
    Pos(-1, 1)   // 8: ↗
)

var maxScore = 0

fun main() {
    val scanner = Scanner(System.`in`)
    val board = Array(4) { Array(4) { Fish(0, 0) } }

    repeat(4) { i ->
        repeat(4) { j ->
            val p = scanner.nextInt()
            val d = scanner.nextInt()
            board[i][j] = Fish(p, d)
        }
    }

    // (0, 0) 초기 세팅
    val firstFish = board[0][0]
    val initScore = firstFish.num
    val initDir = firstFish.dir

    board[0][0] = Fish(-1, initDir) // -1: 술래말

    solve(board, Pos(0, 0), initDir, initScore)

    println(maxScore)
}

fun solve(
    board: Array<Array<Fish>>,
    taggerPos: Pos,
    taggerDir: Int,
    score: Int
) {
    // 1. DFS 현 단계에서 사용할 보드 복사
    val curBoard = Array(4) { i ->
        Array(4) { j -> board[i][j].copy() }
    }

    // 2. 도둑말 이동
    moveFish(curBoard)

    // 3. 술래말 이동 분기 탐색
    var canMove = false
    val dirOffset = directions[taggerDir]

    (1..3).map { step -> taggerPos + (dirOffset * step) }
        .takeWhile { it.inBounds() }
        .filter { pos -> curBoard[pos.x][pos.y].num > 0 }
        .forEach { nextPos ->
            canMove = true

            // 다음 재귀 탐색에 넘겨줄 보드 상태 복사
            val nextBoard = Array(4) { i ->
                Array(4) { j -> curBoard[i][j].copy() }
            }
            val (eatenNum, nextDir) = nextBoard[nextPos.x][nextPos.y]

            // 술래 이동
            nextBoard[taggerPos.x][taggerPos.y] = Fish(0, 0)
            nextBoard[nextPos.x][nextPos.y] = Fish(-1, nextDir)

            // 다음 DFS 탐색
            solve(nextBoard, nextPos, nextDir, score + eatenNum)
        }

    if (!canMove) {
        maxScore = maxOf(maxScore, score)
    }
}

fun moveFish(board: Array<Array<Fish>>) {
    for (num in 1..16) {
        val pos = findFish(board, num) ?: continue
        var curDir = board[pos.x][pos.y].dir

        for (rotate in 0 until 8) {
            val nextPos = pos + directions[curDir]

            if (nextPos.inBounds() && board[nextPos.x][nextPos.y].num != -1) {
                board[pos.x][pos.y].dir = curDir // 이동 성공 시 회전된 방향 확정

                // 위치 스왑
                val temp = board[pos.x][pos.y]
                board[pos.x][pos.y] = board[nextPos.x][nextPos.y]
                board[nextPos.x][nextPos.y] = temp

                break // ★ 이동 완료 시 바로 8방향 회전 루프 탈출!
            }

            // 이동 실패 시 반시계 45도 회전
            curDir = if (curDir == 8) 1 else curDir + 1
        }
    }
}

fun findFish(board: Array<Array<Fish>>, num: Int): Pos? {
    repeat(4) { i ->
        repeat(4) { j ->
            if (board[i][j].num == num) return Pos(i, j)
        }
    }
    return null
}
