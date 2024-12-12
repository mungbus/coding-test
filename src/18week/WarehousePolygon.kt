package `18week`

fun main() {
    val N = readln().toInt()
    var maxH = -1
    val pipes = Array(N) {
        val (L, H) = readln().split(" ").map { it.toInt() }
        if (maxH <= H) {
            maxH = H
        }
        Pair(L, H)
    }
    val pipeMap = pipes.associate { it.first to it.second }
    var currentH = 0
    var answer = 0
    var afterMax = false
    var afterMaxIndex = -1
    val afterMaxList = mutableListOf<Pair<Int, Int>>()
    for (i in pipeMap.keys.min()..pipeMap.keys.max()) {
        val getH = pipeMap.get(i)
        currentH = getH?.let {
            if (it > currentH) it else currentH
        } ?: currentH
        if (afterMax && getH != null) {
            afterMaxList.add(Pair(i, getH))
        }
        if (getH == maxH && afterMaxIndex < i) {
            afterMax = true
            afterMaxIndex = i
        }
        answer += currentH
    }

    val removePart = afterMaxList.filter { it.first > afterMaxIndex }
    var remove = 0
    if (removePart.isNotEmpty()) {
        currentH = 0
        val removeMap = removePart.associate { it.first to it.second }
        for (i in removePart.last().first downTo afterMaxIndex + 1) {
            val getH = removeMap.get(i)
            currentH = getH?.let {
                if (it > currentH) it else currentH
            } ?: currentH
            remove += maxH - currentH
        }
    }
    println(answer - remove)
}
