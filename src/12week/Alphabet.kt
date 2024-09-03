package `12week`

lateinit var map: Array<CharArray>

val direction = arrayOf(
    Pair(0, 1),
    Pair(0, -1),
    Pair(1, 0),
    Pair(-1, 0),
)
var R = 0
var C = 0
var visited = mutableSetOf<Char>()

fun main() {
    val (r, c) = readln().split(" ").map { it.toInt() }
    R = r
    C = c
    map = Array(R) {
        readln().toCharArray()
    }
    println(dfs())
}

operator fun Pair<Int, Int>.plus(pos: Pair<Int, Int>) = Pair(first + pos.first, second + pos.second)

fun dfs(pos: Pair<Int, Int> = Pair(0, 0), depth: Int = 1): Int {
    visited.add(map[pos.first][pos.second])
    return direction.maxOf {
        val nextPos = pos + it
        if (nextPos.first >= 0 && nextPos.second >= 0 && nextPos.first < R && nextPos.second < C && map[nextPos.first][nextPos.second] !in visited) {
            val result = dfs(nextPos, depth + 1)
            visited.remove(map[nextPos.first][nextPos.second])
            result
        } else {
            depth
        }
    }
}
