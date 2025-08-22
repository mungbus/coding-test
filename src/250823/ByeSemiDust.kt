package `250823`

class DustSimulation(private val R: Int, private val C: Int, initialMap: List<List<Int>>) {
    private var map = List(R) { r -> List(C) { c -> initialMap[r][c] }.toMutableList() }.toMutableList()
    private val directions = listOf(0 to -1, 0 to 1, -1 to 0, 1 to 0)

    private val airPurifierPositions = map.indices.filter { map[it][0] == -1 }
    private val top = airPurifierPositions.first()
    private val bottom = airPurifierPositions.last()

    private val topCirculation = buildList {
        // 오른쪽: 공기청정기에서 오른쪽으로
        for (j in 1..<C) add(top to j)
        // 위쪽: 오른쪽에서 위로
        for (i in top - 1 downTo 0) add(i to C - 1)
        // 왼쪽: 위에서 왼쪽으로
        for (j in C - 2 downTo 0) add(0 to j)
        // 아래쪽: 왼쪽에서 아래로
        for (i in 1..<top) add(i to 0)
    }

    private val bottomCirculation = buildList {
        // 오른쪽: 공기청정기에서 오른쪽으로
        for (j in 1..<C) add(bottom to j)
        // 아래쪽: 오른쪽에서 아래로
        for (i in bottom + 1..<R) add(i to C - 1)
        // 왼쪽: 아래에서 왼쪽으로
        for (j in C - 2 downTo 0) add(R - 1 to j)
        // 위쪽: 왼쪽에서 위로
        for (i in R - 2 downTo bottom + 1) add(i to 0)
    }

    private fun spread() {
        val newMap = MutableList(R) { MutableList(C) { 0 } }
        airPurifierPositions.forEach { newMap[it][0] = -1 }

        repeat(R) { r ->
            repeat(C) { c ->
                if (map[r][c] > 0) {
                    val amount = map[r][c] / 5
                    var spreadCount = 0

                    directions.forEach { (dx, dy) ->
                        val nx = r + dx
                        val ny = c + dy
                        if (nx in 0 until R && ny in 0 until C && map[nx][ny] != -1) {
                            newMap[nx][ny] += amount
                            spreadCount++
                        }
                    }
                    newMap[r][c] += map[r][c] - (amount * spreadCount)
                }
            }
        }
        map = newMap
    }

    private fun operate() {
        // 상단 순환
        var dust = 0
        topCirculation.forEach { (x, y) ->
            val temp = map[x][y]
            map[x][y] = dust
            dust = temp
        }

        // 하단 순환
        dust = 0
        bottomCirculation.forEach { (x, y) ->
            val temp = map[x][y]
            map[x][y] = dust
            dust = temp
        }
    }

    fun simulate(T: Int): Int {
        repeat(T) {
            spread()
            operate()
        }
        return map.sumOf { it.sum() } + 2 // 공기청정기 -1 X 2 제외
    }
}

fun main() {
    val (R, C, T) = readln().split(" ").map { it.toInt() }
    val map = List(R) { readln().split(" ").map { it.toInt() } }
    println(DustSimulation(R, C, map).simulate(T))
}
