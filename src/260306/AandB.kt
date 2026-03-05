package `260306`

fun main() {
    val S = readln().trim()
    val T = readln().trim()

    fun canTransform(current: String): Boolean {
        if (current == S) return true
        if (current.length <= S.length) return false

        // 연산 1의 역: 마지막이 A면 제거
        if (current.last() == 'A') {
            if (canTransform(current.dropLast(1))) return true
        }

        // 연산 2의 역: 마지막이 B면 제거 후 뒤집기
        if (current.last() == 'B') {
            if (canTransform(current.dropLast(1).reversed())) return true
        }

        return false
    }

    println(if (canTransform(T)) 1 else 0)
}
