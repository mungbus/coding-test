package `260111`

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.LinkedList
import java.util.Queue

data class Direction(val dx: Int, val dy: Int)

private const val LINE_SIZE = 5
private const val TEAM_SIZE = 7
private const val MIN_DASOM = 4

fun main() {
    // 상하좌우 4방향
    val directions = arrayOf(
        Direction(-1, 0),
        Direction(1, 0),
        Direction(0, -1),
        Direction(0, 1)
    )

    val br = BufferedReader(InputStreamReader(System.`in`))
    // 5x5 여학생 반 정보 입력
    val womanClass = Array(LINE_SIZE) { br.readLine().toCharArray() }

    // 25개 좌표 중 7개를 선택하는 모든 조합 생성
    val allPositions = (0 until LINE_SIZE * LINE_SIZE).toList()
    val combinations = combination(allPositions, TEAM_SIZE)

    // 유효한 팀 개수 카운트
    var answer = 0
    combinations.forEach { team ->
        if (isValidTeam(team, womanClass, directions)) {
            answer++
        }
    }
    println(answer)
}

/**
 * 리스트에서 num개를 선택하는 모든 조합을 반환
 * @param list 원본 리스트
 * @param num 선택할 개수
 * @return 모든 조합의 리스트
 */
fun <T> combination(list: List<T>, num: Int): List<List<T>> {
    // 1개를 선택하는 경우, 각 원소를 단일 리스트로 반환
    if (num == 1) {
        return list.map { listOf(it) }
    }
    val result = mutableListOf<List<T>>()
    for (i in list.indices) {
        val current = list[i]
        // 현재 원소 이후의 리스트에서 (num-1)개를 선택하는 조합 생성
        val combinations = combination(list.subList(i + 1, list.size), num - 1)
        // 현재 원소를 각 조합의 앞에 추가
        val attach = combinations.map { listOf(current) + it }
        result.addAll(attach)
    }
    return result
}

/**
 * 선택된 팀이 유효한지 검증
 * 조건 1: 7명이 모두 인접해 있어야 함 (BFS로 확인)
 * 조건 2: 다솜파('S')가 4명 이상이어야 함
 * @param team 선택된 팀원들의 위치 리스트 (1차원 좌표)
 * @param womanClass 5x5 여학생 반 정보
 * @param directions 상하좌우 방향 배열
 * @return 유효한 팀이면 true, 아니면 false
 */
fun isValidTeam(
    team: List<Int>,
    womanClass: Array<CharArray>,
    directions: Array<Direction>
): Boolean {
    val queue: Queue<Int> = LinkedList()
    val visited = BooleanArray(TEAM_SIZE)
    queue.add(team[0])

    var connectedCount = 1  // 연결된 팀원 수 (첫 번째 팀원부터 시작)
    var dasomCount = 0      // 다솜파 학생 수

    // BFS로 팀원들이 인접해 있는지 확인
    while (queue.isNotEmpty()) {
        val position = queue.poll()
        val row = position / LINE_SIZE
        val col = position % LINE_SIZE

        // 다솜파('S')인 경우 카운트 증가
        if (womanClass[row][col] == 'S') {
            dasomCount++
        }

        // 4방향으로 인접한 팀원 찾기
        directions.forEach { direction ->
            for (i in 1 until TEAM_SIZE) {
                // 아직 방문하지 않았고, 현재 위치와 인접한 팀원인지 확인
                if (!visited[i] &&
                    (col + direction.dx) == (team[i] % LINE_SIZE) &&
                    (row + direction.dy) == (team[i] / LINE_SIZE)
                ) {
                    visited[i] = true
                    queue.add(team[i])
                    connectedCount++
                }
            }
        }
    }

    // 7명이 모두 연결되어 있고, 다솜파가 4명 이상인지 확인
    return connectedCount == TEAM_SIZE && dasomCount >= MIN_DASOM
}
