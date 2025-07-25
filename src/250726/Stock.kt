package `250726`

fun main() {
    val N = readln().toInt()
    repeat(N) {
        readln()
        val stocks = readln().split(" ").map { it.toInt() }
        var maxPrice = 0
        var maxProfit = 0L

        for (i in stocks.indices.reversed()) {
            if (stocks[i] > maxPrice) {
                maxPrice = stocks[i]
            }
            maxProfit += maxPrice - stocks[i]
        }
        println(maxProfit)
    }
}
