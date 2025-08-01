package `250802`

class Solution {
    fun minCost(startPos: IntArray, homePos: IntArray, rowCosts: IntArray, colCosts: IntArray): Int {
        val (startRow, startCol) = startPos
        val (homeRow, homeCol) = homePos

        var totalCost = 0

        var range = if (startRow < homeRow) startRow + 1..homeRow else homeRow..<startRow
        for (row in range) {
            totalCost += rowCosts[row]
        }

        range = if (startCol < homeCol) startCol + 1..homeCol else homeCol..<startCol
        for (col in range) {
            totalCost += colCosts[col]
        }

        return totalCost
    }
}

fun main() {
    Solution().apply {
        println(minCost(intArrayOf(1, 0), intArrayOf(2, 3), intArrayOf(5, 4, 3), intArrayOf(8, 2, 6, 7)) == 18)
    }
}
