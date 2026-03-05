package `260306`

import kotlin.math.max

/**
 * 2D 좌표평면의 직사각형 정보
 * @param x1 왼쪽 아래 x 좌표
 * @param y1 왼쪽 아래 y 좌표
 * @param x2 오른쪽 위 x 좌표
 * @param y2 오른쪽 위 y 좌표
 * @param id 직사각형 번호 (1부터 N까지)
 */
data class Rectangle(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val id: Int)

fun main() {
    // ============ 1단계: 입력 받기 ============
    // N: 직사각형 개수, K: 칠할 직사각형 개수
    val (n, k) = readln().split(" ").map { it.toInt() }
    val rectangles = mutableListOf<Rectangle>()

    // 각 직사각형의 좌표와 번호를 입력받음
    repeat(n) { i ->
        val (x1, y1, x2, y2) = readln().split(" ").map { it.toInt() }
        rectangles.add(Rectangle(x1, y1, x2, y2, i + 1))  // 직사각형 번호는 1부터 시작
    }

    // ============ 2단계: 좌표 압축 ============
    // 문제: 직사각형들이 겹쳐있을 때, 실제로 보이는 면적을 어떻게 계산할까?
    // 해결: 모든 직사각형의 경계선(x, y 좌표)을 기준으로 격자를 만들기
    // 예: x좌표가 [1, 3, 5, 7]이면 구간은 [1-3), [3-5), [5-7)

    val xCoords = mutableSetOf<Int>()  // 모든 x 경계 좌표 수집
    val yCoords = mutableSetOf<Int>()  // 모든 y 경계 좌표 수집

    for (rect in rectangles) {
        xCoords.add(rect.x1)
        xCoords.add(rect.x2)
        yCoords.add(rect.y1)
        yCoords.add(rect.y2)
    }

    val sortedX = xCoords.sorted()  // x 좌표 정렬: [1, 3, 5, 7, ...]
    val sortedY = yCoords.sorted()  // y 좌표 정렬: [1, 2, 4, 5, ...]

    // ============ 3단계: 격자 셀별 정보 미리 계산 ============
    // 각 격자 셀 (i,j)마다:
    // - 어떤 직사각형이 그 셀을 덮고 있는가?
    // - 그 셀의 면적은 얼마인가?

    // cellInfo[i][j] = (최상위 직사각형의 ID, 셀의 면적)
    // 예: cellInfo[0][1] = (2, 4) → 직사각형 2가 보이고, 면적은 4
    val cellInfo = Array(sortedX.size - 1) { i ->
        Array(sortedY.size - 1) { j ->
            val x1 = sortedX[i]      // 현재 격자 셀의 왼쪽 경계
            val x2 = sortedX[i + 1]  // 현재 격자 셀의 오른쪽 경계
            val y1 = sortedY[j]      // 현재 격자 셀의 아래쪽 경계
            val y2 = sortedY[j + 1]  // 현재 격자 셀의 위쪽 경계

            // 이 셀을 덮는 모든 직사각형 중 "번호가 가장 큰" 것을 찾기
            // 번호가 크면 위에 있다는 뜻 = 그것만 보임
            var maxId = 0
            for (rect in rectangles) {
                // 셀이 직사각형 안에 완전히 포함되는지 확인
                if (rect.x1 <= x1 && x2 <= rect.x2 &&
                    rect.y1 <= y1 && y2 <= rect.y2
                ) {
                    maxId = max(maxId, rect.id)
                }
            }

            // 셀의 면적 계산: (너비) × (높이)
            val cellArea = ((x2 - x1) * (y2 - y1)).toLong()

            Pair(maxId, cellArea)  // (보이는 직사각형 ID, 면적) 저장
        }
    }

    var maxArea = 0L               // 지금까지 찾은 최대 면적
    var bestCombination: List<Int>? = null  // 최대 면적을 주는 조합

    // ============ 4단계: 모든 조합 시도하기 ============
    // N개 중 K개를 선택하는 모든 경우의 수를 시도
    // 백트래킹으로 메모리 효율적으로 구현

    fun tryAllCombinations(start: Int, current: MutableList<Int>) {
        // 종료 조건: K개를 모두 선택했으면
        if (current.size == k) {
            // 이 조합의 면적 계산
            val selectedIds = current.toSet()  // 현재 선택한 직사각형들의 ID

            // 핵심: 격자의 각 셀을 순회하면서
            // "보이는 직사각형"이 선택된 것이면 그 셀의 면적 더하기
            var visibleArea = 0L
            for (i in cellInfo.indices) {
                for (j in cellInfo[i].indices) {
                    val (maxId, cellArea) = cellInfo[i][j]

                    // 이 셀의 최상위 직사각형(maxId)이 선택된 것인가?
                    if (maxId in selectedIds) {
                        // 선택된 것이면 이 셀의 면적을 더함
                        visibleArea += cellArea
                    }
                    // 선택되지 않았으면? 면적에 포함하지 않음 (가려졌으므로)
                }
            }

            // 지금까지의 최고 기록을 갱신했는가?
            if (bestCombination == null || visibleArea > maxArea) {
                maxArea = visibleArea
                bestCombination = current.toList()
            }
            return
        }

        // 재귀: start부터 n까지의 직사각형을 하나씩 시도해보기
        for (i in start..n) {
            current.add(i)                           // i번 직사각형 선택
            tryAllCombinations(i + 1, current)       // 다음 직사각형 선택 시도
            current.removeAt(current.size - 1)       // 선택 취소 (백트래킹)
        }
    }

    // ============ 5단계: 결과 출력 ============
    tryAllCombinations(1, mutableListOf())  // 1번부터 시작해서 조합 탐색
    println(bestCombination?.joinToString(" ") ?: "")  // 최적 조합 출력
}
