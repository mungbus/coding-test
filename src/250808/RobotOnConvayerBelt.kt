package `250808`

fun main() {
    val (N, k) = readln().split(" ").map { it.toInt() }
    val belt = MutableList(2 * N) { Pair(0, false) }
    readln().split(" ").map { it.toInt() }.forEachIndexed { index, durability ->
        belt[index] = Pair(durability, false)
    }
    var step = 0

    while (belt.count { it.first == 0 } < k) {
        step++

        // 벨트 회전
        belt.add(0, belt.removeLast())
        belt[N - 1] = belt[N - 1].copy(second = false) // 내리는 위치에 있는 로봇 제거

        // 로봇 이동
        (N - 2 downTo 0).forEach {
            if (belt[it].second && !belt[it + 1].second && belt[it + 1].first > 0) {
                belt[it] = belt[it].copy(second = false)
                belt[it + 1] = belt[it + 1].copy(first = belt[it + 1].first - 1, second = true)
            }
        }
        belt[N - 1] = belt[N - 1].copy(second = false)

        // 로봇 올리기
        if (belt[0].first > 0) {
            belt[0] = belt[0].copy(first = belt[0].first - 1, second = true)
        }
    }

    println(step)
}
