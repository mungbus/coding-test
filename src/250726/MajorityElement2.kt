package `250726`

class Solution {
    fun majorityElement(nums: IntArray): List<Int> {
        val countMap = mutableMapOf<Int, Int>()
        val result = mutableListOf<Int>()
        val threshold = nums.size / 3

        for (num in nums) {
            countMap[num] = countMap.getOrDefault(num, 0) + 1
        }

        for ((key, value) in countMap) {
            if (value > threshold) {
                result.add(key)
            }
        }

        return result
    }
}

fun main() {
    Solution().apply {
        println(majorityElement(intArrayOf(3, 2, 3)).joinToString(",") == "3")
        println(majorityElement(intArrayOf(1)).joinToString(",") == "1")
        println(majorityElement(intArrayOf(1, 2)).joinToString(",") == "1,2")
    }
}
