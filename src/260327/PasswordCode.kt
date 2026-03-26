package `260327`

fun main() {
    val s = readln().trim()
    val n = s.length

    // 암호가 '0'으로 시작하면 해석 불가
    if (s[0] == '0') {
        println(0)
        return
    }

    // dp[i]는 i번째 인덱스까지의 해석 방법의 수
    // 계산의 편의를 위해 크기를 n + 1로 잡습니다.
    val dp = LongArray(n + 1)
    val MOD = 1000000L

    dp[0] = 1 // 초기값 (아무것도 선택하지 않은 경우 1가지)
    dp[1] = 1 // 첫 번째 글자는 무조건 1가지 (0이 아님이 보장됨)

    for (i in 2..n) {
        val oneDigit = s[i - 1] - '0'
        val twoDigits = (s[i - 2] - '0') * 10 + oneDigit

        // 1. 한 자리 숫자로 해석 가능한 경우 (1~9)
        if (oneDigit in 1..9) {
            dp[i] = (dp[i] + dp[i - 1]) % MOD
        }

        // 2. 두 자리 숫자로 해석 가능한 경우 (10~26)
        if (twoDigits in 10..26) {
            dp[i] = (dp[i] + dp[i - 2]) % MOD
        }
    }

    println(dp[n])
}
