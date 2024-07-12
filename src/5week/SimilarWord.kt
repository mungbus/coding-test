package `5week`

fun main() {
    val N = readln().toInt()
    val wordIndex = (0 ..< N).associate {
        readln() to it
    }
    val words = wordIndex.keys.sorted()
    val matchWords = LinkedHashSet<String>()
    var max = 0
    (0 ..< N - 1).forEach {
        val a = words[it]
        val b = words[it + 1]
        val temp = a.checkMatch(b)
        if (max < temp) {
            matchWords.clear()
            max = temp
            matchWords.add(a)
            matchWords.add(b)
        } else if (temp == max) {
            matchWords.add(a)
            matchWords.add(b)
        }
    }
    val result = matchWords.toList().sortedBy {
        wordIndex[it]
    }
    val startWithValue = result.first().take(max)
    println(result.filter { it.startsWith(startWithValue) }.take(2).joinToString("\n"))
}

private fun String.checkMatch(other: String): Int {
    var m = 0
    this.forEachIndexed { index, c ->
        try {
            val check = other.get(index) == c
            if (check) {
                m++
            } else {
                return m
            }
        } catch (e: Exception) {
            return m
        }
    }
    return m
}
