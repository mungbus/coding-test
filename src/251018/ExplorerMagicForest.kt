package `251018`

import java.util.LinkedList
import java.util.Queue

data class Direction(val dr: Int, val dc: Int)

class ExplorerMagicForest(val R: Int, val C: Int, val K: Int) {
    private lateinit var map: Array<IntArray>
    private lateinit var golems: MutableMap<Int, Golem>
    private var totalScore = 0L

    open inner class Position(val r: Int, val c: Int) {
        fun isInside(): Boolean {
            return r in 1..(R + 2) && c in 1..C
        }
    }

    inner class Golem(r: Int, c: Int, val exitDir: Int, val id: Int) : Position(r, c)

    // 북, 동, 남, 서
    private val directions = listOf(
        Direction(-1, 0), // 북
        Direction(0, 1),  // 동
        Direction(1, 0),  // 남
        Direction(0, -1)  // 서
    )

    fun solve() {
        map = Array(R + 3) { IntArray(C + 2) }
        golems = mutableMapOf()

        for (k in 1..K) {
            val (startC, exitDir) = readln().split(" ").map { it.toInt() }
            resetIfNecessary(moveGolem(Golem(0, startC, exitDir, k)))
        }

        println(totalScore)
    }

    private fun moveGolem(initialGolem: Golem): Golem {
        var golem = initialGolem
        while (true) {
            if (canMoveSouth(golem)) {
                golem = Golem(golem.r + 1, golem.c, golem.exitDir, golem.id)
                continue
            }
            if (canMoveWest(golem)) {
                golem = Golem(golem.r + 1, golem.c - 1, (golem.exitDir + 3) % 4, golem.id)
                continue
            }
            if (canMoveEast(golem)) {
                golem = Golem(golem.r + 1, golem.c + 1, (golem.exitDir + 1) % 4, golem.id)
                continue
            }
            break
        }
        return golem
    }

    private fun canMoveSouth(golem: Golem): Boolean {
        val r = golem.r
        val c = golem.c
        val southPos = Position(r + 2, c)
        val southWestPos = Position(r + 1, c - 1)
        val southEastPos = Position(r + 1, c + 1)

        return southPos.isInside() &&
                southWestPos.isInside() && southEastPos.isInside() &&
                map[southPos.r][southPos.c] == 0 &&
                map[southWestPos.r][southWestPos.c] == 0 &&
                map[southEastPos.r][southEastPos.c] == 0
    }

    private fun canMoveWest(golem: Golem): Boolean {
        val r = golem.r
        val c = golem.c
        if (!Position(r, c - 2).isInside() || !Position(r + 2, c - 1).isInside()) return false

        val checkPoints = listOf(
            Position(r - 1, c - 1), Position(r, c - 2), Position(r + 1, c - 1), // 서쪽
            Position(r + 1, c - 2), Position(r + 2, c - 1), Position(r + 1, c)  // 남쪽
        )
        return checkPoints.all { pos -> pos.isInside() && map[pos.r][pos.c] == 0 }
    }

    private fun canMoveEast(golem: Golem): Boolean {
        val r = golem.r
        val c = golem.c
        if (!Position(r, c + 2).isInside() || !Position(r + 2, c + 1).isInside()) return false

        val checkPoints = listOf(
            Position(r - 1, c + 1), Position(r, c + 2), Position(r + 1, c + 1), // 동쪽
            Position(r + 1, c + 2), Position(r + 2, c + 1), Position(r + 1, c)  // 남쪽
        )
        return checkPoints.all { pos -> pos.isInside() && map[pos.r][pos.c] == 0 }
    }

    private fun resetIfNecessary(golem: Golem) {
        if (golem.r <= 3) {
            map = Array(R + 3) { IntArray(C + 2) }
            golems.clear()
        } else {
            occupyMap(golem)
            golems[golem.id] = golem
            totalScore += findMaxRow(golem)
        }
    }

    private fun occupyMap(golem: Golem) {
        map[golem.r][golem.c] = golem.id
        directions.forEach {
            map[golem.r + it.dr][golem.c + it.dc] = golem.id
        }
    }

    private fun findMaxRow(golem: Golem): Int {
        val q: Queue<Position> = LinkedList()
        val visited = Array(R + 3) { BooleanArray(C + 2) }
        var maxRow = 0

        q.add(Position(golem.r, golem.c))
        visited[golem.r][golem.c] = true

        while (q.isNotEmpty()) {
            val pos = q.poll()
            val r = pos.r
            val c = pos.c
            maxRow = maxOf(maxRow, r - 2)

            val currentGolemId = map[r][c]
            val currentGolem = golems[currentGolemId]!!

            // 현재 골렘 내부 이동
            directions.forEach { dir ->
                val newPos = Position(r + dir.dr, c + dir.dc)
                if (newPos.isInside() && !visited[newPos.r][newPos.c] && map[newPos.r][newPos.c] == currentGolemId) {
                    visited[newPos.r][newPos.c] = true
                    q.add(newPos)
                }
            }

            // 다른 골렘으로 이동 (출구를 통해)
            val exitR = currentGolem.r + directions[currentGolem.exitDir].dr
            val exitC = currentGolem.c + directions[currentGolem.exitDir].dc
            if (r == exitR && c == exitC) {
                directions.forEach {
                    val newPos = Position(r + it.dr, c + it.dc)
                    if (newPos.isInside() && !visited[newPos.r][newPos.c] && map[newPos.r][newPos.c] != 0 && map[newPos.r][newPos.c] != currentGolemId) {
                        visited[newPos.r][newPos.c] = true
                        q.add(newPos)
                    }
                }
            }
        }
        return maxRow
    }
}

fun main() {
    val (R, C, K) = readln().split(" ").map { it.toInt() }
    ExplorerMagicForest(R, C, K).solve()
}
