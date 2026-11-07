package `261108`

class Solution {
    fun trap(height: IntArray): Int {
        val maxWaterLevel = height.maxOrNull() ?: 0

        // 1. 지형 전체에 최고 높이까지 물을 가득 채움 (Flood)
        val waterLevels = IntArray(height.size) { maxWaterLevel }

        // 2. 왼쪽 벽이 열려서 왼쪽으로 물이 빠져나감 (Left Drain)
        var currentSpillLevel = 0
        height.indices.forEach { i ->
            currentSpillLevel = maxOf(currentSpillLevel, height[i])
            waterLevels[i] = minOf(waterLevels[i], currentSpillLevel)
        }

        // 3. 오른쪽 벽이 열려서 오른쪽으로 물이 빠져나감 (Right Drain)
        currentSpillLevel = 0
        height.indices.reversed().forEach { i ->
            currentSpillLevel = maxOf(currentSpillLevel, height[i])
            waterLevels[i] = minOf(waterLevels[i], currentSpillLevel)
        }

        // 4. 최종 안정화된 수위에서 원래 지형 높이를 빼서 고인 물 정산
        return height.indices.sumOf { i -> waterLevels[i] - height[i] }
    }
}
