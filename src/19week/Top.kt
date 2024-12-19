package `19week`

import java.util.LinkedList

fun main() {
    val N = readln().toInt()
    val tops = readln().split(" ").map { it.toInt() }
    val answer = IntArray(N)
    val receiverTops = LinkedList<Pair<Int, Int>>()

    for (i in tops.indices) {
        while (receiverTops.isNotEmpty() && receiverTops.last().first < tops[i]) {
            receiverTops.removeLast()
        }
        answer[i] = if (receiverTops.isEmpty()) 0 else receiverTops.last().second + 1
        receiverTops.add(Pair(tops[i], i))
    }

    println(answer.joinToString(" "))
}
