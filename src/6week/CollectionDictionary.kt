package `6week`

class CollectionDictionary(private val find: String) {
    private val dic = arrayOf('A', 'E', 'I', 'O', 'U')
    private var index: Int = -1

    fun find(word: String = ""): Int? {
        index++
        return if (word == find) index
        else if (word.length >= 5) null
        else dic.firstNotNullOfOrNull {
            find(word + it)
        }
    }
}

class Solution {
    fun solution(word: String): Int {
        return CollectionDictionary(word).find()!!
    }
}

fun main() {
    println(Solution().solution("AAAE"))
}
