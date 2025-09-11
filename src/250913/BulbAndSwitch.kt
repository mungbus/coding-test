package `250913`

fun IntArray.toggle(idx: Int) {
    for (i in idx - 1..idx + 1) {
        if (i in indices) {
            this[i] = 1 - this[i]
        }
    }
}

fun IntArray.applyToggle(after: IntArray, firstClick: Boolean): Int? {
    val n = size
    val temp = copyOf()
    var clicks = 0
    if (firstClick) {
        temp.toggle(0)
        clicks++
    }
    for (i in 1..<n) {
        if (temp[i - 1] != after[i - 1]) {
            temp.toggle(i)
            clicks++
        }
    }

    return if (temp.contentEquals(after)) clicks else null
}

fun IntArray.minClicksToTarget(after: IntArray): Int {
    return setOfNotNull(applyToggle(after, false), applyToggle(after, true)).minOrNull() ?: -1
}

fun main() {
    readln()
    val before = readln().toCharArray().map { it.digitToInt() }.toIntArray()
    val after = readln().toCharArray().map { it.digitToInt() }.toIntArray()
    println(before.minClicksToTarget(after))
}
