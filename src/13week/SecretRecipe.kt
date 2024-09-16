package `13week`

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Scanner

lateinit var scanner: Scanner

fun ask(k: Int): Long {
    println("? $k")
    System.out.flush()
    return scanner.next().toLong()
}

fun gaussElimination(matrix: Array<Array<BigDecimal>>, results: Array<BigDecimal>): Array<BigDecimal> {
    val n = results.size
    val coeffs = Array(n) { BigDecimal.ZERO }
    val mc = MathContext(50, RoundingMode.HALF_UP) // BigDecimal의 연산 정확도 설정

    // 가우스 소거법 상삼각 행렬 변환
    for (i in 0 until n) {
        // 현재 행의 대각 원소가 0이면 아래 행과 교환
        var maxRow = i
        for (k in i + 1 until n) {
            if (matrix[k][i].abs() > matrix[maxRow][i].abs()) {
                maxRow = k
            }
        }

        // 행 교환
        val temp = matrix[i]
        matrix[i] = matrix[maxRow]
        matrix[maxRow] = temp

        val tempResult = results[i]
        results[i] = results[maxRow]
        results[maxRow] = tempResult

        // 대각 원소가 0이면 해를 구할 수 없음
        if (matrix[i][i].compareTo(BigDecimal.ZERO) == 0) {
            throw RuntimeException("No unique solution exists.")
        }

        // 가우스 소거법 적용
        for (k in i + 1 until n) {
            val factor = matrix[k][i].divide(matrix[i][i], mc)
            for (j in i until n) {
                matrix[k][j] = matrix[k][j].subtract(factor.multiply(matrix[i][j], mc), mc)
            }
            results[k] = results[k].subtract(factor.multiply(results[i], mc), mc)
        }
    }

    // 역방향 대입을 통해 해 구하기
    for (i in n - 1 downTo 0) {
        var sum = results[i]
        for (j in i + 1 until n) {
            sum = sum.subtract(matrix[i][j].multiply(coeffs[j], mc), mc)
        }
        coeffs[i] = sum.divide(matrix[i][i], mc)
    }

    return coeffs
}

fun solve(N: Int) {
    val values = LongArray(N + 1)

    // 다항식의 값을 구한다.
    for (i in 0..N) {
        values[i] = ask(i)
    }

    // 가우스 소거법을 사용할 행렬과 결과 벡터를 구성한다.
    val matrix = Array(N + 1) { Array(N + 1) { BigDecimal.ZERO } }
    val results = Array(N + 1) { BigDecimal.ZERO }

    for (i in 0..N) {
        results[i] = BigDecimal(values[i])
        var xValue = BigDecimal.ONE
        for (j in 0..N) {
            matrix[i][j] = xValue
            xValue = xValue.multiply(BigDecimal(i)) // i의 거듭제곱을 계산
        }
    }

    // 가우스 소거법을 통해 계수를 구한다.
    val coeffs = gaussElimination(matrix, results)

    // 구한 계수를 출력한다.
    println("! ${coeffs.joinToString(" ") { it.toBigInteger().toString() }}")
    System.out.flush()
}

fun main() {
    // 입력에서 N 값을 읽어들임
    scanner = Scanner(System.`in`)
    val N = scanner.nextInt()

    // 문제를 해결
    solve(N)
}
