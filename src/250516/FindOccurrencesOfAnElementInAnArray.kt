package `250516`

class Solution {
    fun occurrencesOfElement(nums: IntArray, queries: IntArray, x: Int): IntArray {
        val xIndex = mutableListOf<Int>()
        nums.forEachIndexed { i, v ->
            if (v == x) {
                xIndex.add(i)
            }
        }
        return queries.map {
            xIndex.getOrElse(it - 1) { -1 }
        }.toIntArray()
    }
}

fun main() {
    val sol = Solution()
    println(sol.occurrencesOfElement(intArrayOf(1, 3, 1, 7), intArrayOf(1, 3, 2, 4), 1).contentToString())
}
