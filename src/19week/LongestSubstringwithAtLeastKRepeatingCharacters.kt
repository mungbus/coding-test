package `19week`

class Solution1 {
    fun longestSubstring(s: String, k: Int): Int {
        if (s.isEmpty() || k > s.length) return 0
        if (k <= 1) return s.length

        fun recursive(sub: String): Int {
            if (sub.isEmpty()) return 0

            // 각 문자의 빈도수 계산
            val charCount = mutableMapOf<Char, Int>()
            for (char in sub) {
                charCount[char] = charCount.getOrDefault(char, 0) + 1
            }

            // K번 미만으로 등장하는 문자를 찾음
            val invalidChars = charCount.filter { it.value < k }.keys
            if (invalidChars.isEmpty()) return sub.length

            var maxLen = 0
            var start = 0

            // 문자열을 순회하며 invalidChars를 기준으로 분할
            for (i in sub.indices) {
                if (sub[i] in invalidChars) {
                    // 현재까지의 부분 문자열을 재귀적으로 처리
                    val current = recursive(sub.substring(start, i))
                    if (current > maxLen) {
                        maxLen = current
                    }
                    start = i + 1
                }
            }

            val last = recursive(sub.substring(start))
            if (last > maxLen) {
                maxLen = last
            }

            return maxLen
        }

        return recursive(s)
    }
}

fun main() {
    val sol = Solution1()
    println(sol.longestSubstring("aaabbccdddeee", 3) == 6)
    println(sol.longestSubstring("aabbbcddddd", 2) == 5)
    println(sol.longestSubstring("abcd", 3) == 0)
}
