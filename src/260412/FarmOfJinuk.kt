package `260412`

import java.io.*
import java.util.*

// 입력을 빠르게 받기 위한 클래스
class FastReader {
    lateinit var st: StringTokenizer

    fun next(): String? {
        // 토큰이 없으면 새로운 라인을 읽어옴
        while (!::st.isInitialized || !st.hasMoreElements()) {
            val s = readlnOrNull() ?: return null
            st = StringTokenizer(s)
        }
        return st.nextToken()
    }

    fun nextInt() = next()?.toInt() ?: 0
}

fun main() {
    val fr = FastReader()
    val n = fr.nextInt()
    val m = fr.nextInt()

    if (n == 0) {
        println(0); return
    }

    // 2차원 농장을 1차원 배열로 펼침 (성능 최적화)
    val grid = IntArray(n * n)
    val fruits = mutableSetOf<Int>()

    // M번 씨앗 뿌리기: 마지막에 뿌린 씨앗이 기존 것을 덮어씀
    repeat(m) {
        val x = fr.nextInt()
        val y = fr.nextInt()
        val l = fr.nextInt()
        val f = fr.nextInt()
        val re = minOf(n, x + l)
        val ce = minOf(n, y + l)
        if (f != 0) fruits.add(f) // 0번을 제외한 과일 종류 저장
        for (i in x until re) {
            val offset = i * n
            for (j in y until ce) grid[offset + j] = f
        }
    }

    // [좌표 압축] 과일 번호가 매우 클 수 있으므로 (예: 100, 50000)
    // 이를 1, 2, 3... 처럼 작은 숫자로 매핑하여 배열 인덱스로 쓰기 좋게 만듦
    val sortedFruits = fruits.toIntArray().apply { sort() }
    val comp = IntArray(n * n)
    for (i in 0 until n * n) {
        if (grid[i] != 0) {
            // 이진 탐색으로 실제 과일 번호가 압축된 배열의 몇 번째 인덱스인지 찾음
            comp[i] = Arrays.binarySearch(sortedFruits, grid[i]) + 1
        }
    }

    val K = sortedFruits.size
    // colFreq[j][f]: j번째 열(column)에서 현재 범위(L개 행) 내 과일 f의 개수
    val colFreq = Array(n) { IntArray(K + 1) }
    val colDistinct = IntArray(n)              // 각 열에 포함된 과일 종류 수
    val colHasZero = IntArray(n)               // 각 열에 0번 과일이 포함된 개수
    val globalFreq = IntArray(K + 1)           // 현재 검사 중인 L x L 정사각형 전체의 과일 빈도

    // 변의 길이가 L인 정사각형 중 조건을 만족하는 것이 있는지 확인하는 함수
    fun check(L: Int): Boolean {
        // 매번 check할 때마다 열 상태 초기화
        for (j in 0 until n) {
            colDistinct[j] = 0
            colHasZero[j] = 0
            Arrays.fill(colFreq[j], 0)
        }

        // 1. [초기 행 설정] 0번 행부터 L-1번 행까지의 데이터를 열(column)별로 미리 계산
        for (i in 0 until L) {
            val off = i * n
            for (j in 0 until n) {
                val f = comp[off + j]
                if (f == 0) colHasZero[j]++
                else if (colFreq[j][f]++ == 0) colDistinct[j]++
            }
        }

        // 2. [행 슬라이딩] 행의 범위를 한 칸씩 아래로 내리며 검사
        for (i in 0..n - L) {
            Arrays.fill(globalFreq, 0)
            var totalDistinct = 0    // 전체 정사각형 안의 과일 종류 수
            var badColsInWindow = 0  // 0이 있거나 종류가 2개 넘는 '나쁜 열'의 개수

            // 3. [열 슬라이딩] 가로 방향으로 L 크기의 윈도우를 미끄러지듯 이동
            for (j in 0 until n) {
                // 새로운 열 j 추가
                if (colHasZero[j] > 0 || colDistinct[j] > 2) {
                    badColsInWindow++ // 0이 포함되거나 이미 한 줄에 3종류 이상이면 못 쓰는 열
                } else {
                    // 좋은 열이라면 그 안의 과일들을 전체 카운트에 합산
                    for (fIdx in 1..K) {
                        if (colFreq[j][fIdx] > 0) {
                            if (globalFreq[fIdx] == 0) totalDistinct++
                            globalFreq[fIdx] += colFreq[j][fIdx]
                        }
                    }
                }

                // 윈도우 크기가 L을 초과하면 가장 왼쪽(j-L) 열을 제거
                if (j >= L) {
                    val prevJ = j - L
                    if (colHasZero[prevJ] > 0 || colDistinct[prevJ] > 2) {
                        badColsInWindow--
                    } else {
                        for (fIdx in 1..K) {
                            if (colFreq[prevJ][fIdx] > 0) {
                                globalFreq[fIdx] -= colFreq[prevJ][fIdx]
                                if (globalFreq[fIdx] == 0) totalDistinct--
                            }
                        }
                    }
                }

                // [최종 조건 확인] 윈도우 안에 나쁜 열이 없고, 과일 종류가 1~2개면 성공!
                if (j >= L - 1 && badColsInWindow == 0 && totalDistinct in 1..2) return true
            }

            // [행 업데이트] 다음 행으로 넘어가기 위해 i행 데이터를 빼고 i+L행 데이터를 넣음 (행 단위 슬라이딩)
            if (i + L < n) {
                val rowOut = i * n
                val rowIn = (i + L) * n
                for (j in 0 until n) {
                    // 나가는 행 데이터 제거
                    val fOut = comp[rowOut + j]
                    if (fOut == 0) colHasZero[j]--
                    else if (--colFreq[j][fOut] == 0) colDistinct[j]--

                    // 새로 들어오는 행 데이터 추가
                    val fIn = comp[rowIn + j]
                    if (fIn == 0) colHasZero[j]++
                    else if (colFreq[j][fIn]++ == 0) colDistinct[j]++
                }
            }
        }
        return false
    }

    // 4. [이분 탐색] 가능한 변의 길이를 "스무고개" 하듯 찾아나감
    var low = 1
    var high = n
    var ans = 0
    while (low <= high) {
        val mid = (low + high) / 2
        if (check(mid)) {
            ans = mid         // 성공하면 더 큰 길이를 시도
            low = mid + 1
        } else {
            high = mid - 1    // 실패하면 길이를 줄임
        }
    }
    // 가장 넓은 넓이 출력
    println(ans.toLong() * ans)
}
