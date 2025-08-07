package `250808`

data class Point(val row: Int, val col: Int)

class JumpGame(
    private val N: Int,
    private val k: Int,
    private val map: List<List<Boolean>>,
    private val visited: MutableSet<Point> = mutableSetOf()
) {
    fun solve(pos: Point = Point(0, 0)): Boolean {
        if (pos.col >= N - 1) return true
        if (!visited.add(pos)) return false

        return listOfNotNull(
            tryMove { go(pos) },
            tryMove { down(pos) },
            tryMove { jump(pos) }
        ).any { next -> solve(next) }
    }

    private fun go(pos: Point): Point? {
        val next = Point(pos.row, pos.col + 1)
        return next.takeIf {
            it.col >= N || (it.col < map[0].size && map[it.row][it.col])
        }
    }

    private fun down(pos: Point): Point? {
        val next = Point(pos.row, pos.col - 1)
        return next.takeIf {
            it.col >= 0 && map[it.row][it.col]
        }
    }

    private fun jump(pos: Point): Point? {
        val next = Point(
            if (pos.row == 0) 1 else 0,
            pos.col + k + 1
        )
        return next.takeIf {
            it.col >= N || (it.col < map[0].size && map[it.row][it.col])
        }
    }

    private inline fun tryMove(move: () -> Point?): Point? = try {
        move()
    } catch (e: Exception) {
        null
    }
}

fun main() {
    val (N, k) = readln().split(" ").map { it.toInt() }
    val map = listOf(
        readln().map { it.digitToInt() == 1 },
        readln().map { it.digitToInt() == 1 }
    )

    println(if (JumpGame(N, k, map).solve()) 1 else 0)
}
