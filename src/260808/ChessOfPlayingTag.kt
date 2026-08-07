package `260808`

import java.util.Scanner

/**
 * 2D 체스판 위에서의 위치(좌표)를 다루는 클래스
 * @property x 세로 위치 (행, 0~3)
 * @property y 가로 위치 (열, 0~3)
 */
data class Pos(val x: Int, val y: Int) {
    // [연산자 오버로딩] 좌표 + 좌표 ➔ Pos(1, 2) + Pos(0, 1) = Pos(1, 3)
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)

    // [연산자 오버로딩] 방향 * 이동거리 ➔ Pos(0, 1) * 2 = Pos(0, 2)
    operator fun times(scale: Int): Pos = Pos(x * scale, y * scale)

    // 현재 좌표가 4x4 체스판 안쪽(0~3)에 안전하게 있는지 확인하는 함수
    fun inBounds(): Boolean = x in 0..3 && y in 0..3

    // ★ 보드에서 이 좌표(Pos) 위치에 있는 말을 바로 읽어오는 메서드
    fun of(board: Array<Array<Piece>>): Piece = board[x][y]

    // ★ 보드의 이 좌표(Pos) 위치에 새로운 말을 배치하는 메서드
    fun setTo(board: Array<Array<Piece>>, piece: Piece) {
        board[x][y] = piece
    }
}

/**
 * 체스판 한 칸에 들어갈 말의 정보
 * @property num 도둑말 번호 (1~16: 도둑말, 0: 빈 칸, -1: 술래말)
 * @property dir 말의 이동 방향 (1~8번 방향)
 */
data class Piece(var num: Int, var dir: Int)

// 1번부터 8번까지의 방향 오프셋 배열 (0번 인덱스는 dummy)
// 1: ↑, 2: ↖, 3: ←, 4: ↙, 5: ↓, 6: ↘, 7: →, 8: ↗
val directions = arrayOf(
    Pos(0, 0),
    Pos(-1, 0),  // 1: 위 (↑)
    Pos(-1, -1), // 2: 왼쪽 위 (↖)
    Pos(0, -1),  // 3: 왼쪽 (←)
    Pos(1, -1),  // 4: 왼쪽 아래 (↙)
    Pos(1, 0),   // 5: 아래 (↓)
    Pos(1, 1),   // 6: 오른쪽 아래 (↘)
    Pos(0, 1),   // 7: 오른쪽 (→)
    Pos(-1, 1)   // 8: 오른쪽 위 (↗)
)

// 술래가 얻을 수 있는 점수의 최댓값을 기록할 전역 변수
var maxScore = 0

fun main() {
    val scanner = Scanner(System.`in`)
    val board = Array(4) { Array(4) { Piece(0, 0) } }

    // 4x4 체스판 입력을 반복문(repeat)으로 받음
    repeat(4) { i ->
        repeat(4) { j ->
            val p = scanner.nextInt() // 도둑말 번호
            val d = scanner.nextInt() // 도둑말 방향
            board[i][j] = Piece(p, d)
        }
    }

    // --- [게임 시작: 1단계] ---
    // 술래가 맨 처음 (0, 0) 위치로 들어가며 그곳의 도둑말을 꿀꺽 먹습니다!
    val startPos = Pos(0, 0)
    val firstThief = startPos.of(board)
    val initScore = firstThief.num // 점수 획득
    val initDir = firstThief.dir   // 도둑말의 방향을 술래가 물려받음

    // (0, 0) 위치에 술래(-1)를 배치
    startPos.setTo(board, Piece(-1, initDir))

    // 모든 갈래길(경우의 수)을 탐색하는 DFS 백트래킹 탐색 시작
    solve(board, startPos, initDir, initScore)

    // 최종적으로 찾은 가장 높은 점수 출력
    println(maxScore)
}

// 모든 경우의 수를 하나씩 다 따라가보는 재귀 함수 (DFS 백트래킹)
fun solve(
    board: Array<Array<Piece>>,
    taggerPos: Pos, // 현재 술래 위치
    taggerDir: Int, // 현재 술래가 쳐다보는 방향
    score: Int      // 현재까지 누적된 점수
) {
    // ★ 매우 중요: 이 갈래길 전용 "체스판 복사본"을 만든다!
    // (다른 갈래길을 탐색하러 돌아왔을 때 체스판이 엉망이 되어있지 않도록 독립된 판을 만듦)
    val curBoard = Array(4) { i ->
        Array(4) { j -> board[i][j].copy() }
    }

    // --- [2단계: 도둑말 전체 이동] ---
    // 1번부터 16번 도둑말들이 차례대로 한 칸씩 움직입니다.
    moveThieves(curBoard)

    // --- [3단계: 술래말의 이동 (갈림길 생성)] ---
    var canMove = false
    val dirOffset = directions[taggerDir] // 술래가 바라보는 방향의 1칸 이동 벡터

    // 술래는 1칸, 2칸, 3칸 이동 중에서 선택할 수 있음
    (1..3)
        .map { step -> taggerPos + (dirOffset * step) }   // 1~3칸 이동했을 때의 목표 좌표 계산
        .takeWhile { it.inBounds() }                     // 체스판 밖으로 나가면 직진 불가(탐색 중단)
        .filter { pos -> pos.of(curBoard).num > 0 }       // pos.of(curBoard)로 도둑말이 있는 칸만 필터링
        .forEach { nextPos ->
            canMove = true // 하나라도 갈 수 있는 곳이 있으면 계속 게임 진행!

            // 다음 갈래길에 넘겨줄 독립적인 체스판을 새로 만듦
            val nextBoard = Array(4) { i ->
                Array(4) { j -> curBoard[i][j].copy() }
            }

            // 먹을 도둑말의 (번호, 방향)을 pos.of()로 깔끔하게 추출
            val (eatenNum, nextDir) = nextPos.of(nextBoard)

            // 술래 이동 처리: pos.setTo() 사용으로 가독성 극대화
            taggerPos.setTo(nextBoard, Piece(0, 0))    // 원래 술래 위치는 빈 칸(0)
            nextPos.setTo(nextBoard, Piece(-1, nextDir)) // 새 위치는 술래(-1) 및 새 방향

            // 다음 턴 진행! (재귀 호출)
            solve(nextBoard, nextPos, nextDir, score + eatenNum)
        }

    // 술래가 쳐다보는 방향에 더 이상 먹을 도둑말이 하나도 없다면 게임 끝!
    if (!canMove) {
        maxScore = maxOf(maxScore, score) // 최고 점수 갱신
    }
}

// 1번부터 16번 도둑말을 순서대로 한 칸씩 움직여주는 함수
fun moveThieves(board: Array<Array<Piece>>) {
    for (num in 1..16) {
        // 체스판에서 num번 도둑말이 어디 있는지 위치를 찾음 (이미 잡혔으면 null)
        val pos = findThief(board, num) ?: continue
        var curDir = pos.of(board).dir

        // 제자리에서 반시계 방향으로 돌려보며 갈 수 있는 방향 탐색 (최대 8번)
        for (rotate in 0 until 8) {
            val nextPos = pos + directions[curDir]

            // 1) 체스판 안쪽이고, 2) 술래(-1)가 있는 칸이 아니면 이동 가능!
            if (nextPos.inBounds() && nextPos.of(board).num != -1) {
                // 회전해서 정해진 최종 방향을 저장
                pos.of(board).dir = curDir

                // 두 칸의 위치를 Swap (pos.of / pos.setTo 조합으로 깔끔하게 교환)
                val temp = pos.of(board)
                pos.setTo(board, nextPos.of(board))
                nextPos.setTo(board, temp)

                break // 성공적으로 이동했으니 8방향 회전 루프 탈출!
            }

            // 앞이 막혔다면 반시계 방향으로 45도 회전 (1->2->...->8->1)
            curDir = if (curDir == 8) 1 else curDir + 1
        }
    }
}

// 체스판 전체(4x4)를 하나하나 훑어서 특정 번호(num)의 도둑말 위치를 찾아주는 함수
fun findThief(board: Array<Array<Piece>>, num: Int): Pos? {
    repeat(4) { i ->
        repeat(4) { j ->
            val pos = Pos(i, j)
            if (pos.of(board).num == num) return pos
        }
    }
    return null // 도둑말이 이미 술래한테 먹혀서 없으면 null 반환
}
