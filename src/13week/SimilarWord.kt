package `13week`

fun main() {
    val N = readln().toInt()
    val baseWord = readln()
    var answer = 0

    repeat(N - 1) {
        val compare = baseWord.toMutableList()
        val word = readln().toList()
        var cnt = 0

        for (w in word) {
            if (compare.contains(w)) {
                compare.remove(w)
            } else {
                cnt++
            }
        }

        if (cnt < 2 && compare.size < 2) {
            answer++
        }
    }
    println(answer)
}
