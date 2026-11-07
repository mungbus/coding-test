package `261108`

class Solution {
    fun trap(height: IntArray): Int {
        val waterLevels = IntArray(height.size)

        // 1. 왼쪽 벽 기준으로 물이 차오를 수 있는 최대 수위 기록
        var currentSpillLevel = 0
        height.indices.forEach { i ->
            currentSpillLevel = maxOf(currentSpillLevel, height[i])
            waterLevels[i] = currentSpillLevel
        }

        // 2. 오른쪽 벽 기준으로 넘치는 물을 빼면서 동시에 남은 물의 양 정산
        var totalWater = 0
        currentSpillLevel = 0
        height.indices.reversed().forEach { i ->
            currentSpillLevel = maxOf(currentSpillLevel, height[i])
            val finalWaterLevel = minOf(waterLevels[i], currentSpillLevel)
            totalWater += finalWaterLevel - height[i]
        }

        return totalWater
    }
}
