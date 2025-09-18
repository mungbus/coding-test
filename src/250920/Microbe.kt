package `250920`

data class Microbe(val r1: Int, val c1: Int, val r2: Int, val c2: Int) {
    val area: Int
        get() = (r2 - r1 + 1) * (c2 - c1 + 1)
}

lateinit var board: Array<IntArray>
val microbes = mutableListOf<Microbe>()

fun main() {
    val (N, Q) = readln().split(" ").map { it.toInt() }
    board = Array(N) { IntArray(N) { 0 } }

    repeat(Q) {
        val (r1, c1, r2, c2) = readln().split(" ").map { it.toInt() - 1 }
        val adjustedR1 = r1.coerceIn(0, N - 1)
        val adjustedC1 = c1.coerceIn(0, N - 1)
        val adjustedR2 = r2.coerceIn(0, N - 1)
        val adjustedC2 = c2.coerceIn(0, N - 1)

        val newMicrobe = Microbe(adjustedR1, adjustedC1, adjustedR2, adjustedC2)
        microbes.add(newMicrobe)

        // 기존 미생물과 겹치는 부분 교체
        for (i in adjustedR1..adjustedR2) {
            for (j in adjustedC1..adjustedC2) {
                board[i][j] = it + 1
            }
        }

        // 정렬 및 좌측 하단 배치
        arrangeMicrobes(N)

        // 인접 미생물 계산
        println(calculateAdjacentProduct(N))
    }
}

fun arrangeMicrobes(N: Int) {
    microbes.sortByDescending { it.area } // 넓이 기준 정렬
    val newBoard = Array(N) { IntArray(N) { 0 } }
    val remainingMicrobes = mutableListOf<Microbe>()

    for ((index, microbe) in microbes.withIndex()) {
        var placed = false
        for (i in 0 until N) {
            for (j in 0 until N) {
                if (canPlace(newBoard, microbe, i, j)) {
                    placeMicrobe(newBoard, microbe, i, j, index + 1)
                    placed = true
                    remainingMicrobes.add(microbe)
                    break
                }
            }
            if (placed) break
        }
    }
    microbes.clear()
    microbes.addAll(remainingMicrobes)
    board = newBoard
}

fun canPlace(newBoard: Array<IntArray>, microbe: Microbe, startX: Int, startY: Int): Boolean {
    val (r1, c1, r2, c2) = microbe
    if (startX + (r2 - r1) >= newBoard.size || startY + (c2 - c1) >= newBoard[0].size) return false

    for (i in 0..(r2 - r1)) {
        for (j in 0..(c2 - c1)) {
            if (newBoard[startX + i][startY + j] != 0) return false
        }
    }
    return true
}

fun placeMicrobe(newBoard: Array<IntArray>, microbe: Microbe, startX: Int, startY: Int, id: Int) {
    val (r1, c1, r2, c2) = microbe
    for (i in 0..(r2 - r1)) {
        for (j in 0..(c2 - c1)) {
            newBoard[startX + i][startY + j] = id
        }
    }
}

fun calculateAdjacentProduct(N: Int): Int {
    val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
    var totalSum = 0

    for (i in 0..<N) {
        for (j in 0..<N) {
            if (board[i][j] == 0) continue
            val currentMicrobe = board[i][j]
            for ((dx, dy) in directions) {
                val nx = i + dx
                val ny = j + dy
                if (nx in 0..<N && ny in 0..<N && board[nx][ny] != 0 && board[nx][ny] != currentMicrobe) {
                    totalSum += currentMicrobe * board[nx][ny]
                }
            }
        }
    }
    return totalSum
}
