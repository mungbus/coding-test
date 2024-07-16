package `5week`

fun main() {
    val (n, m) = readln().splitMapToInt()
    val setDictionary = mutableMapOf<Int, Set<Int>>()
    repeat(n) {
        setDictionary[it] = setOf(it)
    }
    repeat(m) {
        val (command, a, b) = readln().splitMapToInt()
        if (command == 1) {
            val result = if (a == b) "YES"
            else if (setDictionary[a] == setDictionary[b]) "YES"
            else "NO"
            println(result)
        } else {
            val assignSet = (setDictionary[a] ?: emptySet()) + (setDictionary[b] ?: emptySet())
            assignSet.forEach {
                setDictionary[it] = assignSet
            }
        }
    }
}

fun String.splitMapToInt() = split(" ").map { it.toInt() }
