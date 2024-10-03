package `17week`

fun main() {
    val N = readln().toInt()
    val answer = if (N == 1) {
        1
    } else {
        val goldList = Array(N) { it + 1 }.toList()
        goldList.divThree().first()
    }
    println("! $answer")
    System.out.flush()
}

fun List<Int>.redo(): List<Int> {
    return if (size > 1) {
        divThree()
    } else this
}

fun List<Int>.divThree(): List<Int> {
    val div = size / 3
    val mod = size % 3
    val result = when (mod) {
        2 -> chunked(div + 1)
        1 -> chunked(div).let {
            val (first, second) = it
            listOfNotNull(first, second, it.getOrElse(2) { emptyList() } + it.getOrElse(3) { emptyList() })
        }

        else -> chunked(div)
    }
    val (first, second) = result
    println("? ${first.joinToString(" ")} 0 ${second.joinToString(" ")} 0")
    System.out.flush()
    val check = readln()
    return when (check) {
        "<" -> first.redo()
        ">" -> second.redo()
        else -> result.last().redo()
    }
}
