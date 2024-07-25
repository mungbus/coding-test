package `6week`

import java.util.LinkedList

class CollectionDictionary(private val find: String) {
    private val dic = arrayOf('A', 'E', 'I', 'O', 'U')
    private val wordList = LinkedList<String>()

    fun find(word: String = ""): Int? {
        wordList.add(word)
        return if (word == find) wordList.size - 1
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
