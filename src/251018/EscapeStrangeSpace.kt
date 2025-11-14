package `251018`

import java.util.LinkedList
import java.util.Queue

/**
 * 문제 개요:
 * - N x N 크기의 평면에 정육면체가 놓여있고, 플레이어는 정육면체 내부에서 시작
 * - 정육면체를 탈출한 후 평면을 이동하여 최종 탈출구에 도달해야 함
 * - 평면에는 시간에 따라 확산되는 장애물(시간 이상 현상)이 존재
 *
 * 해결 전략:
 * 1단계: 정육면체 내부 BFS - 시작점에서 정육면체 탈출구까지 최단 경로
 * 2단계: 평면 BFS - 정육면체 탈출 후 최종 목적지까지 최단 경로 (시간 이상 현상 회피)
 *
 * 정육면체 구조:
 * - 5개의 면으로 구성 (동서남북 측면 4개 + 윗면 1개)
 * - 각 면은 M x M 크기의 2차원 배열
 * - 면 인덱스: 0=동쪽, 1=서쪽, 2=남쪽, 3=북쪽, 4=윗면
 */

// 디버그 옵션
private const val DEBUG_CUBE = false
private const val DEBUG_PLANE = false

// 평면 상태 출력용 상수
private const val CELL_WALL = "■"           // 벽 (값 1)
private const val CELL_CUBE = "□"           // 정육면체 (값 3)
private const val CELL_EXIT = "E"           // 탈출구 (값 4)
private const val CELL_EMPTY = "."          // 빈 공간 (값 0)
private const val CELL_PLAYER = "P"         // 플레이어 현재 위치
private const val CELL_VISITED = "+"        // 방문한 위치
private const val CELL_OBSTACLE = "*"       // 시간 이상 현상
private const val CELL_START = "S"          // 정육면체 시작점
private const val CELL_CUBE_END = "G"       // 정육면체 탈출점

/**
 * 시간에 따라 확산되는 장애물 정보
 * @param position 장애물의 시작 위치 (행, 열)
 * @param direction 확산 방향 (0:동, 1:서, 2:남, 3:북)
 * @param velocity 확산 속도 (한 칸 이동하는데 걸리는 시간)
 */
data class SpreadingObstacle(val position: Pair<Int, Int>, val direction: Int, val velocity: Int)

/**
 * 이상한 공간 탈출 솔버 클래스
 * @param N 평면의 크기 (N x N)
 * @param M 정육면체 각 면의 크기 (M x M)
 * @param plane N x N 크기의 평면 맵 (0:빈공간, 1:벽, 3:정육면체, 4:탈출구)
 * @param faces 정육면체의 5개 면 정보 (0:빈공간, 1:벽, 2:시작점)
 * @param spreadingObstacles 시간 이상 현상 목록
 */
class EscapeStrangeSpace(
    private val N: Int,
    private val M: Int,
    private val plane: Array<IntArray>,
    private val faces: List<Array<IntArray>>,
    private val spreadingObstacles: List<SpreadingObstacle>
) {
    // 방향: 0:동, 1:서, 2:남, 3:북
    private val directions = arrayOf(
        0 to 1,   // 동
        0 to -1,  // 서
        1 to 0,   // 남
        -1 to 0   // 북
    )

    // 정육면체 면: idx: 0=동, 1=서, 2=남, 3=북, 4=윗면
    private val cube = Array(5) { Array(M) { IntArray(M) } }

    // 시작점과 끝점 정보
    private var startIdx = 0  // 정육면체 내부 시작 면 인덱스
    private var startPosition = 0 to 0  // 정육면체 내부 시작 위치

    private var endIdx = 0  // 정육면체에서 탈출해야 할 면 인덱스
    private var endPosition = 0 to 0  // 정육면체 탈출 위치

    private var middlePosition = 0 to 0  // 정육면체를 탈출한 직후 평면에서의 위치
    private var finalPosition = 0 to 0  // 최종 목표 위치 (평면의 탈출구)

    // Pair 연산 확장 함수
    private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

    // 좌표 출력용 확장 함수
    private fun Pair<Int, Int>.toCoordString() = "($first, $second)"

    // 디버그 출력 함수
    private fun debugCube(message: () -> String) {
        if (DEBUG_CUBE) println(message())
    }

    private fun debugPlane(message: () -> String) {
        if (DEBUG_PLANE) println(message())
    }


    // 시간별 장애물 위치를 저장하는 맵 (각 칸에 장애물이 도달하는 시간 저장)
    private lateinit var timeMap: Array<IntArray>

    /**
     * 메인 솔루션 함수
     * @return 탈출에 필요한 최소 시간, 불가능하면 -1
     */
    fun solve(): Int {
        // 정육면체 데이터 복사 및 시작점 찾기
        repeat(5) { i ->
            repeat(M) { r ->
                repeat(M) { c ->
                    cube[i][r][c] = faces[i][r][c]
                    if (cube[i][r][c] == 2) {
                        startIdx = i
                        startPosition = r to c
                        cube[i][r][c] = 0  // 시작 위치는 빈 공간으로 처리
                    }
                }
            }
        }

        // 평면에서 정육면체 위치와 출구 찾기
        var cubeStartPosition = 0 to 0
        repeat(N) { i ->
            repeat(N) { j ->
                if (plane[i][j] == 4) {
                    finalPosition = i to j
                }
                if (plane[i][j] == 3) {
                    cubeStartPosition = i to j
                }
            }
        }

        // 탈출해야 할 면 찾기
        val (cubeStartR, cubeStartC) = cubeStartPosition
        for (i in cubeStartR - M + 1..cubeStartR) {
            for (j in cubeStartC - M + 1..cubeStartC) {
                repeat(4) { k ->
                    val nextPos = (i to j) + directions[k]
                    val (nr, nc) = nextPos
                    if (nr in 0 until N && nc in 0 until N && plane[nr][nc] == 0) {
                        middlePosition = nextPos
                        endIdx = k
                        endPosition = when (k) {
                            0 -> M - 1 to cubeStartR - nr
                            1 -> M - 1 to M - (cubeStartR - nr) - 1
                            2 -> M - 1 to M - (cubeStartC - nc) - 1
                            3 -> M - 1 to cubeStartC - nc
                            else -> 0 to 0
                        }
                        return@repeat
                    }
                }
            }
        }

        debugCube { "시작: idx=$startIdx, pos=${startPosition.toCoordString()}" }
        debugCube { "끝: idx=$endIdx, pos=${endPosition.toCoordString()}" }
        debugCube { "중간: pos=${middlePosition.toCoordString()}, 최종: pos=${finalPosition.toCoordString()}" }

        // 시간 이상 현상 맵 생성
        buildTimeMap()

        // 1단계: 정육면체 내부 BFS
        val cubeTime = cubeBFS()
        if (cubeTime == 0) return -1

        // 2단계: 평면 BFS
        val planeTime = planeBFS(cubeTime + 1)
        return if (planeTime == 0) -1 else planeTime
    }

    /**
     * 시간 이상 현상의 확산 경로를 계산하여 timeMap에 저장
     * 각 칸에 장애물이 언제 도달하는지 시간을 기록
     */
    private fun buildTimeMap() {
        timeMap = Array(N) { IntArray(N) }
        spreadingObstacles.forEach { obs ->
            val (startPos, d, v) = obs
            var currentPos = startPos
            val (startR, startC) = startPos
            timeMap[startR][startC] = 1

            for (step in 1..N) {
                val time = step * v
                currentPos += directions[d]
                val (cr, cc) = currentPos

                if (cr !in 0 until N || cc !in 0 until N) break
                if (currentPos == finalPosition) break

                if (plane[cr][cc] == 0) {
                    timeMap[cr][cc] = time
                } else {
                    break
                }
            }
        }
    }

    /**
     * 정육면체 내부에서 BFS를 수행하여 탈출구까지의 최단 경로 탐색
     * @return 탈출에 걸린 시간, 탈출 불가능하면 0
     */
    private fun cubeBFS(): Int {
        val queue: Queue<Triple<Int, Pair<Int, Int>, Int>> = LinkedList()
        val visited = Array(5) { Array(M) { BooleanArray(M) } }

        queue.add(Triple(startIdx, startPosition, 0))
        val (startR, startC) = startPosition
        visited[startIdx][startR][startC] = true

        debugCube { "\n=== 정육면체 BFS 시작 ===" }
        debugCube { "시작 위치: 면 $startIdx, ${startPosition.toCoordString()}" }
        debugCube { "목표 위치: 면 $endIdx, ${endPosition.toCoordString()}" }
        printCubeUnfolded(startIdx, startPosition, visited, 0)

        while (queue.isNotEmpty()) {
            val (idx, position, time) = queue.poll()

            if (idx == endIdx && position == endPosition) {
                debugCube { "\n정육면체 탈출 성공! 소요 시간: $time" }
                return time
            }

            repeat(4) { k ->
                val (nr, nc) = position + directions[k]

                if (idx == 4) {
                    // 윗면일 때
                    if (nr in 0 until M && nc in 0 until M) {
                        // 윗면 내부 이동
                        tryMoveWithinFace(idx, nr, nc, visited, queue, time)
                    } else {
                        // 윗면에서 측면으로 이동
                        val nextIdx = k
                        val transformedPos = when {
                            nr < 0 -> 0 to (M - nc - 1)  // 북쪽으로
                            nr >= M -> 0 to nc           // 남쪽으로
                            nc < 0 -> 0 to nr            // 서쪽으로
                            nc >= M -> 0 to (M - nr - 1) // 동쪽으로
                            else -> nr to nc
                        }
                        tryMoveToFace(nextIdx, transformedPos, visited, queue, idx, time)
                    }
                } else {
                    // 측면일 때
                    if (nr in 0 until M && nc in 0 until M) {
                        // 측면 내부 이동
                        tryMoveWithinFace(idx, nr, nc, visited, queue, time)
                    } else {
                        // 측면 간 이동
                        handleSideTransition(idx, nr, nc, position.second, visited, queue, time)
                    }
                }
            }
        }

        debugCube { "\n정육면체 탈출 실패!" }
        return 0
    }

    /**
     * 정육면체 전개도를 콘솔에 시각적으로 출력 (디버깅용)
     * 전개도 형태:
     *       [북쪽(3)]
     * [서쪽(1)] [윗면(4)] [동쪽(0)]
     *       [남쪽(2)]
     */
    private fun printCubeUnfolded(
        currentIdx: Int,
        currentPos: Pair<Int, Int>,
        visited: Array<Array<BooleanArray>>,
        time: Int
    ) {
        if (!DEBUG_CUBE) return

        // 전개도 형태로 배치:
        //       [북쪽(3)]
        // [서쪽(1)] [윗면(4)] [동쪽(0)]
        //       [남쪽(2)]

        val faceNames = arrayOf("동쪽", "서쪽", "남쪽", "북쪽", "윗면")

        println("\n┌" + "─".repeat(M * 3 * 2 + 5) + "┐")

        // 북쪽 면 (3) 출력
        repeat(M) { r ->
            print("│" + " ".repeat(M * 2 + 3))
            repeat(M) { c ->
                val cell = getCubeCell(3, r to c, currentIdx, currentPos, visited)
                print("$cell ")
            }
            println(" ".repeat(M * 2 + 2) + "│")
        }

        println("│" + "─".repeat(M * 3 * 2 + 5) + "│")

        // 서쪽(1), 윗면(4), 동쪽(0) 출력
        repeat(M) { r ->
            print("│ ")

            // 서쪽(1)
            repeat(M) { c ->
                val cell = getCubeCell(1, r to c, currentIdx, currentPos, visited)
                print("$cell ")
            }
            print("│ ")

            // 윗면(4)
            repeat(M) { c ->
                val cell = getCubeCell(4, r to c, currentIdx, currentPos, visited)
                print("$cell ")
            }
            print("│ ")

            // 동쪽(0)
            repeat(M) { c ->
                val cell = getCubeCell(0, r to c, currentIdx, currentPos, visited)
                print("$cell ")
            }
            println("│")
        }

        println("│" + "─".repeat(M * 3 * 2 + 5) + "│")

        // 남쪽 면 (2) 출력
        repeat(M) { r ->
            print("│" + " ".repeat(M * 2 + 3))
            repeat(M) { c ->
                val cell = getCubeCell(2, r to c, currentIdx, currentPos, visited)
                print("$cell ")
            }
            println(" ".repeat(M * 2 + 2) + "│")
        }

        println("└" + "─".repeat(M * 3 * 2 + 5) + "┘")

        // 범례와 정보
        if (time == 0) {
            println("\n[정육면체 전개도]")
            println("면 배치: 0:동쪽 1:서쪽 2:남쪽 3:북쪽 4:윗면")
            println("[범례] $CELL_PLAYER:현재위치 $CELL_EXIT:탈출구 $CELL_WALL:벽 $CELL_EMPTY:빈공간 $CELL_VISITED:방문함 $CELL_OBSTACLE:시간이상현상 $CELL_START:시작점 $CELL_CUBE_END:탈출점")
        }
        println("현재: 면 ${faceNames[currentIdx]} ${currentPos.toCoordString()}, 시간: $time")
    }

    /**
     * 정육면체의 특정 칸을 출력용 문자로 변환
     * @return 셀 상태를 나타내는 문자열
     */
    private fun getCubeCell(
        faceIdx: Int,
        pos: Pair<Int, Int>,
        currentIdx: Int,
        currentPos: Pair<Int, Int>,
        visited: Array<Array<BooleanArray>>
    ): String {
        val (r, c) = pos

        return when {
            faceIdx == currentIdx && pos == currentPos -> CELL_PLAYER
            faceIdx == startIdx && pos == startPosition -> CELL_START
            faceIdx == endIdx && pos == endPosition -> CELL_CUBE_END
            visited[faceIdx][r][c] -> CELL_VISITED
            cube[faceIdx][r][c] == 1 -> CELL_WALL
            cube[faceIdx][r][c] == 0 -> CELL_EMPTY
            else -> "?"
        }
    }

    /**
     * 정육면체의 다른 면으로 이동 시도
     * 중복 코드 제거를 위한 헬퍼 함수
     * @return 이동 성공 여부
     */
    private fun tryMoveToFace(
        nextIdx: Int,
        transformedPos: Pair<Int, Int>,
        visited: Array<Array<BooleanArray>>,
        queue: Queue<Triple<Int, Pair<Int, Int>, Int>>,
        currentIdx: Int,
        time: Int
    ): Boolean {
        if (nextIdx == -1) return false

        val (tnr, tnc) = transformedPos
        if (!visited[nextIdx][tnr][tnc] && cube[nextIdx][tnr][tnc] == 0) {
            visited[nextIdx][tnr][tnc] = true
            queue.add(Triple(nextIdx, transformedPos, time + 1))

            debugCube { "\n[시간 ${time + 1}] 면 $currentIdx → 면 $nextIdx ${transformedPos.toCoordString()}로 이동" }
            printCubeUnfolded(nextIdx, transformedPos, visited, time + 1)
            return true
        }
        return false
    }

    /**
     * 같은 면 내부에서 이동 시도
     * 중복 코드 제거를 위한 헬퍼 함수
     * @return 이동 성공 여부
     */
    private fun tryMoveWithinFace(
        idx: Int,
        nr: Int,
        nc: Int,
        visited: Array<Array<BooleanArray>>,
        queue: Queue<Triple<Int, Pair<Int, Int>, Int>>,
        time: Int
    ): Boolean {
        if (!visited[idx][nr][nc] && cube[idx][nr][nc] == 0) {
            visited[idx][nr][nc] = true
            queue.add(Triple(idx, nr to nc, time + 1))

            debugCube { "\n[시간 ${time + 1}] 면 $idx ${(nr to nc).toCoordString()}로 이동" }
            printCubeUnfolded(idx, nr to nc, visited, time + 1)
            return true
        }
        return false
    }

    /**
     * 정육면체 측면 간 이동 처리
     * 측면에서 경계를 넘어갈 때 다른 면으로의 전환 로직
     * - 위로 나가면: 윗면(4)으로 이동
     * - 좌우로 나가면: 인접한 측면으로 회전 이동
     */
    private fun handleSideTransition(
        idx: Int,
        nr: Int,
        nc: Int,
        c: Int,
        visited: Array<Array<BooleanArray>>,
        queue: Queue<Triple<Int, Pair<Int, Int>, Int>>,
        time: Int
    ) {
        when {
            nr < 0 -> {
                // 윗면으로 이동
                val nextIdx = 4
                val transformedPos = when (idx) {
                    0 -> c to 0
                    1 -> (M - c - 1) to (M - 1)
                    2 -> (M - 1) to c
                    3 -> 0 to (M - c - 1)
                    else -> 0 to 0
                }
                tryMoveToFace(nextIdx, transformedPos, visited, queue, idx, time)
            }

            nc < 0 -> {
                // 왼쪽으로 (측면 회전 - 반시계 방향)
                val nextIdx = when (idx) {
                    0 -> 2  // 동 → 남
                    1 -> 3  // 서 → 북
                    2 -> 1  // 남 → 서
                    3 -> 0  // 북 → 동
                    else -> -1
                }
                val transformedPos = nr to (M - 1)
                tryMoveToFace(nextIdx, transformedPos, visited, queue, idx, time)
            }

            nc >= M -> {
                // 오른쪽으로 (측면 회전 - 시계 방향)
                val nextIdx = when (idx) {
                    0 -> 3  // 동 → 북
                    1 -> 2  // 서 → 남
                    2 -> 0  // 남 → 동
                    3 -> 1  // 북 → 서
                    else -> -1
                }
                val transformedPos = nr to 0
                tryMoveToFace(nextIdx, transformedPos, visited, queue, idx, time)
            }
        }
    }

    /**
     * 평면에서 BFS를 수행하여 최종 탈출구까지의 최단 경로 탐색
     * 시간 이상 현상을 회피하면서 이동
     * @param startTime 평면 BFS 시작 시간 (정육면체 탈출 시간 + 1)
     * @return 최종 탈출에 걸린 총 시간, 탈출 불가능하면 0
     */
    private fun planeBFS(startTime: Int): Int {
        val queue: Queue<Pair<Pair<Int, Int>, Int>> = LinkedList()
        val visited = Array(N) { BooleanArray(N) }
        val visitOrder = mutableListOf<Pair<Pair<Int, Int>, Int>>()  // 방문 순서 기록

        queue.add(middlePosition to startTime)
        val (mr, mc) = middlePosition
        visited[mr][mc] = true
        visitOrder.add(middlePosition to startTime)

        debugPlane { "\n=== 평면 BFS 시작 ===" }
        debugPlane { "시작 위치: ${middlePosition.toCoordString()}, 시작 시간: $startTime" }
        printPlaneState(middlePosition, visited, startTime)

        while (queue.isNotEmpty()) {
            val (position, time) = queue.poll()

            if (position == finalPosition) {
                debugPlane { "\n목표 도달! 총 소요 시간: $time" }
                debugPlane { "방문한 칸 수: ${visitOrder.size}" }
                return time
            }

            for (k in 0..3) {
                val nextPos = position + directions[k]
                val (nr, nc) = nextPos

                if (nr in 0 until N && nc in 0 until N && !visited[nr][nc] && plane[nr][nc] != 1) {
                    if (canMoveTo(nextPos, time + 1)) {
                        visited[nr][nc] = true
                        queue.add(nextPos to (time + 1))
                        visitOrder.add(nextPos to (time + 1))

                        debugPlane { "\n[시간 ${time + 1}] ${nextPos.toCoordString()}로 이동" }
                        printPlaneState(nextPos, visited, time + 1)
                    }
                }
            }
        }

        debugPlane { "\n목표에 도달할 수 없음!" }
        return 0
    }

    /**
     * 평면의 현재 상태를 콘솔에 시각적으로 출력 (디버깅용)
     */
    private fun printPlaneState(currentPos: Pair<Int, Int>, visited: Array<BooleanArray>, currentTime: Int) {
        if (!DEBUG_PLANE) return

        println("\n┌" + "─".repeat(N * 2 + 1) + "┐")
        repeat(N) { i ->
            print("│ ")
            repeat(N) { j ->
                val pos = i to j
                val cell = when {
                    pos == currentPos -> CELL_PLAYER
                    pos == finalPosition -> CELL_EXIT
                    plane[i][j] == 1 -> CELL_WALL
                    plane[i][j] == 3 -> CELL_CUBE
                    timeMap[i][j] in 1..currentTime -> CELL_OBSTACLE
                    visited[i][j] -> CELL_VISITED
                    else -> CELL_EMPTY
                }
                print("$cell ")
            }
            println("│")
        }
        println("└" + "─".repeat(N * 2 + 1) + "┘")

        // 범례 출력
        if (currentPos == middlePosition) {
            println("\n[범례] $CELL_PLAYER:현재위치 $CELL_EXIT:탈출구 $CELL_WALL:벽 $CELL_CUBE:정육면체 $CELL_EMPTY:빈공간 $CELL_VISITED:방문함 $CELL_OBSTACLE:시간이상현상")
        }
    }

    /**
     * 특정 시간에 특정 위치로 이동 가능한지 확인
     * 시간 이상 현상이 해당 시간에 그 위치에 있는지 검사
     * @return 이동 가능하면 true, 불가능하면 false
     */
    private fun canMoveTo(position: Pair<Int, Int>, time: Int): Boolean {
        val (r, c) = position
        return when {
            timeMap[r][c] == 0 -> true
            timeMap[r][c] > time -> true
            else -> false
        }
    }
}

/**
 * 프로그램 진입점
 * 입력 형식:
 * - 첫 줄: N(평면 크기), M(정육면체 면 크기), F(시간 이상 현상 개수)
 * - 다음 N줄: N x N 평면 정보
 * - 다음 5 * M줄: 정육면체 5개 면의 M x M 정보
 * - 다음 F줄: 각 시간 이상 현상의 (시작행, 시작열, 방향, 속도)
 */
fun main() {
    val (N, M, F) = readln().split(" ").map { it.toInt() }

    val plane = Array(N) { readln().split(" ").map { it.toInt() }.toIntArray() }

    val faces = List(5) {
        Array(M) {
            readln().split(" ").map { it.toInt() }.toIntArray()
        }
    }

    val obstacles = mutableListOf<SpreadingObstacle>()
    repeat(F) {
        val input = readln().split(" ").map { it.toInt() }
        obstacles.add(SpreadingObstacle(input[0] to input[1], input[2], input[3]))
    }

    val escapeStrangeSpace = EscapeStrangeSpace(N, M, plane, faces, obstacles)
    println(escapeStrangeSpace.solve())
}
