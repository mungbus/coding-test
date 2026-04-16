package `260417`

const val DIVIDER = 1000

fun main() {
    // n: 행렬 크기, b: 거듭제곱 횟수 (B가 매우 클 수 있으므로 Long으로 처리)
    val (n, b) = readln().split(" ").map { it.toLong() }

    // 초기 행렬 입력 및 1000으로 나눈 나머지 처리
    // (B가 1일 경우를 대비해 미리 나머지 연산을 해두는 것이 안전합니다)
    val matrix = Array(n.toInt()) {
        readln().split(" ").map { it.toInt() % DIVIDER }.toIntArray()
    }
    val result = power(matrix, b, n.toInt())
    result.forEach { println(it.joinToString(" ")) }
}

/**
 * 거듭제곱을 수행하는 함수 (분할 정복)
 * A^n = A^(n/2) * A^(n/2) 원리를 이용함
 */
fun power(a: Array<IntArray>, exp: Long, n: Int): Array<IntArray> {
    // 지수가 1이면 자기 자신을 반환 (재귀의 끝)
    if (exp == 1L) {
        return a
    }

    // 지수를 절반으로 나누어 재귀적으로 계산 (O(log B) 시간 복잡도의 핵심)
    val half = power(a, exp / 2, n)

    // 절반짜리 행렬을 서로 곱함 (예: A^2 * A^2 = A^4)
    val multiplied = multiply(half, half, n)

    // 만약 지수가 홀수라면, 위에서 구한 짝수 승수에 원본 행렬(a)을 한 번 더 곱해줌
    // (예: A^5 = A^2 * A^2 * A)
    return if (exp % 2 == 1L) {
        multiply(multiplied, a, n)
    } else {
        multiplied
    }
}

/**
 * 두 행렬의 곱셈을 수행하는 함수
 * 결과값의 각 원소는 1000으로 나눈 나머지를 저장함
 */
fun multiply(m1: Array<IntArray>, m2: Array<IntArray>, n: Int): Array<IntArray> {
    val temp = Array(n) { IntArray(n) }
    repeat(n) { i ->
        repeat(n) { j ->
            var sum = 0L
            repeat(n) { k ->
                // 곱셈 과정에서 Int 범위를 넘을 수 있으므로 Long으로 합산
                sum += (m1[i][k].toLong() * m2[k][j].toLong())
            }
            // 최종 결과를 1000으로 나눈 나머지로 저장
            temp[i][j] = (sum % DIVIDER).toInt()
        }
    }
    return temp
}
