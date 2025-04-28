package `25week`

class StringChecker(private val start: String) {
    fun check(end: String): String? {
        val endLengthIdx = end.length - 1
        val strStart = end[0]
        val strEnd = end[endLengthIdx]

        var next1: String? = null
        var next2: String? = null
        if (strStart == 'B') {
            next1 = end.substring(1).reversed()
            if (next1 == start) return next1
        }
        if (strEnd == 'A') {
            next2 = end.substring(0, endLengthIdx)
            if (next2 == start) return next2
        }

        if (!next1.isNullOrEmpty()) {
            check(next1)?.let {
                return it
            }
        }
        if (!next2.isNullOrEmpty()) {
            check(next2)?.let {
                return it
            }
        }
        return null
    }
}

fun main() {
    val S = readln()
    val sc = StringChecker(S)
    val T = readln()
    println(sc.check(T)?.let { 1 } ?: 0)
}
