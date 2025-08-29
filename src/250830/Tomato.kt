package `250830`

class Pos(val h: Int, val n: Int, val m: Int) {
    operator fun plus(other: Pos) = Pos(h + other.h, n + other.n, m + other.m)

    fun getTomato(tomatos: Array<Array<IntArray>>): Int = tomatos[h][n][m]
    fun setTomato(tomatos: Array<Array<IntArray>>, value: Int) {
        tomatos[h][n][m] = value
    }

    fun includes(H: Int, N: Int, M: Int): Boolean = h in 0..<H && n in 0..<N && m in 0..<M
}

fun main() {
    val (M, N, H) = readln().split(" ").map { it.toInt() }
    val tomatos = Array(H) {
        Array(N) {
            readln().split(" ").map { it.toInt() }.toIntArray()
        }
    }
    val queue = ArrayDeque<Pos>()
    val directions = listOf(
        Pos(1, 0, 0),
        Pos(-1, 0, 0),
        Pos(0, 1, 0),
        Pos(0, -1, 0),
        Pos(0, 0, 1),
        Pos(0, 0, -1)
    )

    var unripeCount = 0
    repeat(H) { h ->
        repeat(N) { n ->
            repeat(M) { m ->
                val pos = Pos(h, n, m)
                when (pos.getTomato(tomatos)) {
                    1 -> queue.add(pos) // 익은 토마토 위치 추가
                    0 -> unripeCount++ // 익지 않은 토마토 개수 증가
                }
            }
        }
    }

    var days = 0
    while (queue.isNotEmpty() && unripeCount > 0) {
        val size = queue.size
        repeat(size) {
            val cur = queue.removeFirst()
            directions.forEach { dir ->
                val next = cur + dir
                if (next.includes(H, N, M) && next.getTomato(tomatos) == 0) {
                    next.setTomato(tomatos, 1)
                    unripeCount--
                    queue.add(next)
                }
            }
        }
        days++
    }
    println(if (unripeCount == 0) days else -1)
}
