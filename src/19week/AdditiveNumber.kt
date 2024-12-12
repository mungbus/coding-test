package `19week`

class Solution {
    fun isAdditiveNumber(num: String): Boolean {
        val n = num.length

        // 첫 번째 숫자의 끝 인덱스 i는 1부터 n/2까지
        for (i in 1..n / 2) {
            // 첫 번째 숫자가 '0'으로 시작할 경우, 숫자가 '0'인 경우만 유효
            if (num[0] == '0' && i > 1) break

            val first = num.substring(0, i).toLong()

            // 두 번째 숫자의 끝 인덱스 j는 i+1부터 i + (n - i)/2까지
            for (j in i + 1..i + (n - i) / 2) {
                // 두 번째 숫자가 '0'으로 시작할 경우, 숫자가 '0'인 경우만 유효
                if (num[i] == '0' && j - i > 1) break
                val second = num.substring(i, j).toLong()
                // 현재 시작 인덱스 j부터 끝까지 수열을 검증
                if (validate(num, j, first, second)) {
                    return true
                }
            }
        }
        return false
    }

    private fun validate(num: String, start: Int, first: Long, second: Long): Boolean {
        val n = num.length
        var currentStart = start
        var prev1 = first
        var prev2 = second

        while (currentStart < n) {
            val sum = prev1 + prev2
            val sumStr = sum.toString()
            val end = currentStart + sumStr.length

            // 문자열의 끝을 초과하면 실패
            if (end > n) return false

            // 다음 부분 문자열이 합과 일치하는지 확인
            val nextStr = num.substring(currentStart, end)
            if (nextStr != sumStr) return false

            // 일치하면 다음 숫자로 진행
            prev1 = prev2
            prev2 = sum
            currentStart = end
        }

        return true
    }
}

fun main() {
    val sol = Solution()
    println(sol.isAdditiveNumber("112358"))
    println(sol.isAdditiveNumber("199100199"))
}
