package `261005`

/**
 * 모든 마을을 '발전소가 정확히 하나인 트리' 여러 개로 나누는 최소 도로 제거 비용을 구한다.
 *
 * 접근 흐름:
 * 1. 제거 비용 = 전체 도로 비용 - 남긴 도로 비용이므로, 남긴 비용을 최대화한다.
 * 2. 제거 비용이 비싼 도로부터 확인한다 (최대 신장 숲을 만드는 크루스칼 알고리즘).
 * 3. 사이클이 생기거나 발전소 두 개가 연결되는 도로는 제거하고, 나머지는 유지한다.
 * 4. 모든 도로를 처리한 후에도 발전소가 없는 묶음이 있으면 불가능하므로 -1을 반환한다.
 *
 * 시간 복잡도: O(M log M + (N + M) α(N)). 도로 정렬이 대부분의 시간을 차지한다.
 * 공간 복잡도: O(N + M). N은 마을 수, M은 도로 수이며 α(N)은 사실상 상수 수준이다.
 */
class Microgrid {
    /**
     * 두 마을을 잇는 양방향 도로. removalCost는 건설 비용이 아니라 '제거할 때 내는 비용'이다.
     * 따라서 이 값이 클수록 유지하는 것이 유리하다. 같은 두 마을 사이의 도로도 각각 관리한다.
     */
    data class Road(val from: Int, val to: Int, val removalCost: Long)

    /**
     * 유지한 도로로 연결된 마을 묶음을 Union-Find로 관리한다.
     * 각 묶음은 항상 사이클이 없는 트리이고, 발전소는 최대 하나라는 조건을 유지한다.
     * 발전소가 없는 묶음도 중간 과정에서는 허용하며, 마지막에 전력 공급 여부를 검사한다.
     */
    private class VillageGroups(villageCount: Int, powerPlants: IntArray) {
        // 마을 번호는 1부터 시작한다. 처음에는 각 마을이 자기 자신을 대표하는 독립 묶음이다.
        private val parent = IntArray(villageCount + 1) { it }
        // size와 hasPowerPlant는 묶음의 대표 인덱스에서만 유효한 정보다.
        private val size = IntArray(villageCount + 1) { 1 }
        private val hasPowerPlant = BooleanArray(villageCount + 1)

        init {
            for (village in powerPlants) hasPowerPlant[village] = true
        }

        /** 마을이 속한 묶음의 대표를 찾는다. 재귀 없이 탐색하므로 호출 스택을 사용하지 않는다. */
        private fun find(village: Int): Int {
            var current = village
            // 부모를 조부모로 바꾸는 경로 압축: 이후 같은 묶음을 더 적은 이동으로 찾는다.
            while (parent[current] != current) {
                parent[current] = parent[parent[current]]
                current = parent[current]
            }
            return current
        }

        /** 도로를 유지할 수 있으면 두 묶음을 합치고 true, 제거해야 하면 false를 반환한다. */
        fun connectIfAllowed(from: Int, to: Int): Boolean {
            var first = find(from)
            var second = find(to)

            // 이미 두 마을 사이에 경로가 있다. 도로를 추가하면 사이클이 생긴다.
            // 같은 두 마을 사이에 도로가 여러 개 있는 경우에도 이 검사로 중복 연결을 막는다.
            if (first == second) return false
            // 발전소가 있는 두 묶음을 합치면 발전소가 두 개가 된다.
            if (hasPowerPlant[first] && hasPowerPlant[second]) return false

            // 작은 묶음을 큰 묶음에 붙여 트리의 깊이를 제한한다.
            if (size[first] < size[second]) {
                val temporary = first
                first = second
                second = temporary
            }
            parent[second] = first
            size[first] += size[second]
            // 발전소 0+0개 또는 1+0개만 합쳐지므로, 합친 묶음에도 발전소가 최대 하나다.
            hasPowerPlant[first] = hasPowerPlant[first] || hasPowerPlant[second]
            return true
        }

        /** 최종 조건인 '각 묶음에 발전소가 정확히 하나'를 만족하는지 확인한다. */
        fun hasUnpoweredVillage(): Boolean {
            for (village in 1 until parent.size) {
                if (!hasPowerPlant[find(village)]) return true
            }
            return false
        }
    }

    /**
     * 도로 선택 순서와 제거 비용의 합산을 담당한다. 연결 가능 여부는 VillageGroups에 맡긴다.
     *
     * 비싼 도로부터 선택해도 되는 이유:
     * 모든 발전소를 가상의 정점에 미리 연결했다고 생각하면, 발전소 두 개를 잇는 도로도
     * 사이클을 만든다. 따라서 두 거절 조건은 모두 일반 크루스칼의 사이클 검사에 해당한다.
     * 발전소를 잇는 가상 도로를 고정한 채 최대 신장 트리를 구하는 것과 같은 선택이다.
     */
    private class PowerGrid(
        private val groups: VillageGroups,
        private val roads: Array<Road>
    ) {
        fun minimumRemovalCost(): Long {
            // 이 배열을 직접 정렬한다. 제거 비용이 비싼 도로를 먼저 유지할 기회를 준다.
            roads.sortByDescending { it.removalCost }
            var removedCost = 0L
            for (road in roads) {
                if (!groups.connectIfAllowed(road.from, road.to)) {
                    // 유지한 도로는 비용을 내지 않는다. 거절한 도로의 제거 비용만 더한다.
                    removedCost += road.removalCost
                }
            }
            // 도로를 모두 확인했는데 발전소가 없는 묶음이 남았다면, 원래 도로망에서도
            // 그 묶음은 발전소에 도달할 수 없다. 도로 제거만으로는 연결을 새로 만들 수 없다.
            return if (groups.hasUnpoweredVillage()) -1L else removedCost
        }
    }

    /** 입력으로 만든 객체들을 연결해 최소 제거 비용을 반환한다. roads는 계산 중 정렬된다. */
    fun calculateMinimumRemovalCost(
        villageCount: Int,
        powerPlants: IntArray,
        roads: Array<Road>
    ): Long {
        val grid = PowerGrid(VillageGroups(villageCount, powerPlants), roads)
        // 도로 하나의 비용은 최대 10^9지만, 합은 최대 3 × 10^14이므로 Long을 사용한다.
        return grid.minimumRemovalCost()
    }
}

/** 입력 읽기 → 도로 객체 생성 → 최소 비용 계산 → 정답 출력 순서로 실행한다. */
fun main() {
    // 첫 줄: N(마을 수), M(도로 수), K(발전소 수).
    val (villageCount, roadCount, powerPlantCount) = readln().split(" ").map { it.toInt() }
    // 둘째 줄: 발전소가 있는 K개 마을 번호. 해당 마을의 묶음에 발전소 표시를 한다.
    val powerPlants = readln().split(" ").map { it.toInt() }.toIntArray()
    require(powerPlants.size == powerPlantCount)

    // 다음 M줄: 출발 마을, 도착 마을, 제거 비용. 비용은 합산을 위해 Long으로 변환한다.
    val roads = Array(roadCount) {
        val (from, to, removalCost) = readln().split(" ").map { it.toInt() }
        Microgrid.Road(from, to, removalCost.toLong())
    }

    println(Microgrid().calculateMinimumRemovalCost(villageCount, powerPlants, roads))
}
