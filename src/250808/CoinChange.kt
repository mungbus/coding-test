package `250808`

class CoinChange {
    companion object {
        private const val INIT_VALUE = -1
    }

    fun coinChange(coins: IntArray, amount: Int): Int {
        if (amount == 0) return 0
        if (coins.isEmpty()) return INIT_VALUE
        val dp = IntArray(amount + 1) { INIT_VALUE }
        dp[0] = 0
        coins.forEach { coin ->
            (coin..amount).forEach {
                val beforeCoin = it - coin
                if (dp[beforeCoin] != INIT_VALUE) {
                    listOf(dp[it], dp[beforeCoin] + 1).filter { it != INIT_VALUE }.minOrNull()?.let { result ->
                        dp[it] = result
                    }
                }
            }
        }
        return dp[amount]
    }
}

fun main() {
    val cc = CoinChange()
    println(cc.coinChange(intArrayOf(1, 2, 5), 11) == 3) // 11 = 5 + 5 + 1
    println(cc.coinChange(intArrayOf(2), 3) == -1)
    println(cc.coinChange(intArrayOf(1), 0) == 0)
}
