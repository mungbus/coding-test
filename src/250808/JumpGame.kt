package `250808`

class JumpGame(
    val N: Int,
    val k: Int,
    val map: List<List<Boolean>>,
    private val pos: Pair<Int, Int> = Pair(0, 0),
    val rm: Int = -1
) {
    fun go(): Pair<Int, Int> {
        val newPos = Pair(pos.first, pos.second + 1)
        if (newPos.second >= map[0].size || !map[newPos.first][newPos.second]) {
            throw IllegalArgumentException("Cannot move forward from position $pos")
        }
        return newPos
    }

    fun down(): Pair<Int, Int> {
        val newPos = Pair(pos.first, pos.second - 1)
        if (newPos.second <= rm || !map[newPos.first][newPos.second]) {
            throw IllegalArgumentException("Cannot move backward from position $pos")
        }
        return newPos
    }

    fun jump(): Pair<Int, Int> {
        val nextRow = if (pos.first == 0) 1 else 0
        val nextCol = pos.second + k + 1
        val nextPos = Pair(nextRow, nextCol)

        if (nextCol < map[0].size && !map[nextPos.first][nextPos.second]) {
            throw IllegalArgumentException("Cannot jump to position $nextPos")
        }
        return nextPos
    }

    fun isFinished(): Boolean {
        return pos.second >= N - 1
    }
}

fun main() {
    val (N, k) = readln().split(" ").map { it.toInt() }
    val map = listOf(
        readln().map { it.digitToInt() == 1 },
        readln().map { it.digitToInt() == 1 }
    )

    println(if (jump(JumpGame(N, k, map, rm = -1))) 1 else 0)
}

fun jump(game: JumpGame): Boolean {
    if (game.isFinished()) return true
    val go = try {
        val next = game.go()
        jump(JumpGame(game.N, game.k, game.map, next, game.rm + 1))
    } catch (e: IllegalArgumentException) {
        false
    }
    val down = try {
        val next = game.down()
        jump(JumpGame(game.N, game.k, game.map, next, game.rm + 1))
    } catch (e: IllegalArgumentException) {
        false
    }
    val jump = try {
        val next = game.jump()
        jump(JumpGame(game.N, game.k, game.map, next, game.rm + 1))
    } catch (e: IllegalArgumentException) {
        false
    }
    return go || down || jump
}
