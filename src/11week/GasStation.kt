package `11week`

import java.util.LinkedList
import java.util.Queue

fun main() {
    val N = readln().toInt()
    val distance = readln().split(" ").map { it.toLong() }

    val priceQueue: Queue<Int> = LinkedList(readln().split(" ").map {
        it.toInt()
    })

    var answer = 0L
    var index = 0
    while (index < N - 1) {
        val basePrice = priceQueue.poll()
        var tempDistance = 0L
        tempDistance += distance[index++]
        while (priceQueue.isNotEmpty() && priceQueue.peek() > basePrice && distance.size > index) {
            tempDistance += distance[index++]
        }
        answer += tempDistance * basePrice
    }
    println(answer)
}
