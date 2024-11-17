package `19week`

class Solution2 {
    fun partitionString(s: String): Int {
        val seen = mutableSetOf<Char>()
        var count = 1
        for (char in s) {
            if (char in seen) {
                count++
                seen.clear()
            }
            seen.add(char)
        }
        return count
    }
}

fun main() {
    val result1 = Solution2().partitionString("abacaba")
    println(result1)
    val result2 = Solution2().partitionString("ssssss")
    println(result2)
}
