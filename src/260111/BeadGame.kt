package `260111`

fun main() {
    val (b1, b2, b3) = readln().split(" ").map { it.toInt() }
    val moves = listOf(b1, b2, b3)

    // dp[i][j] = (i, j) 상태에서 현재 플레이어가 이길 수 있는가?
    // null = 아직 계산 안 함, true = 이김, false = 짐
    val dp = Array(501) { Array<Boolean?>(501) { null } }

    /**
     * (k1, k2) 상태에서 현재 플레이어가 이길 수 있는지 판단
     *
     * 승리 조건: 내가 선택할 수 있는 수 중에 하나라도
     *           상대방이 지는 상태로 만들 수 있으면 내가 이김
     *
     * 패배 조건: 어떤 선택을 해도 상대방이 이기는 상태가 되면 내가 짐
     */
    fun canWin(k1: Int, k2: Int): Boolean {
        // 이미 계산한 경우
        if (dp[k1][k2] != null) return dp[k1][k2]!!

        // 두 통 모두 비어있으면 이동 불가 -> 현재 플레이어 패배
        if (k1 == 0 && k2 == 0) {
            dp[k1][k2] = false
            return false
        }

        // 첫 번째 통에서 구슬을 꺼내는 경우
        for (move in moves) {
            if (k1 >= move) {
                // (k1 - move, k2) 상태에서 상대방이 지면 내가 이김
                if (!canWin(k1 - move, k2)) {
                    dp[k1][k2] = true
                    return true
                }
            }
        }

        // 두 번째 통에서 구슬을 꺼내는 경우
        for (move in moves) {
            if (k2 >= move) {
                // (k1, k2 - move) 상태에서 상대방이 지면 내가 이김
                if (!canWin(k1, k2 - move)) {
                    dp[k1][k2] = true
                    return true
                }
            }
        }

        // 어떤 선택을 해도 상대방이 이기는 상황 -> 내가 짐
        dp[k1][k2] = false
        return false
    }

    repeat(5) {
        val (k1, k2) = readln().split(" ").map { it.toInt() }

        // A가 먼저 시작하므로, (k1, k2)에서 A가 이길 수 있으면 "A", 아니면 "B"
        if (canWin(k1, k2)) {
            println("A")
        } else {
            println("B")
        }
    }
}
