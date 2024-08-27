package `11week`

import java.util.LinkedList

fun main() {
    val (_, _, M) = readln().split(" ").map { it.toLong() }
    val initVideoList = readln().split(" ").map { it.toInt() }
    val initVideoQueue = LinkedList(initVideoList)
    val viewMap: Map<Int, Int> = readln().split(" ").mapIndexed { i, v -> i + 1 to v.toInt() }.toMap()
    val cycleMap = mutableMapOf<Int, LinkedList<Int>>()
    val nonCycleMap = mutableMapOf<Int, LinkedList<Int>>()

    while (!initVideoQueue.isEmpty()) {
        val init = initVideoQueue.poll()
        var v = init
        var isCycle = false
        while (!isCycle) {
            cycleMap.getOrPut(init) { LinkedList<Int>() }.apply {
                val next = viewMap[v]!!
                if (contains(next)) {
                    var nonCycleValue = peek()
                    while (nonCycleValue != next) {
                        val nonCycle = nonCycleMap.getOrPut(init) { LinkedList<Int>() }
                        nonCycle.add(poll())
                        nonCycleValue = peek()
                    }
                    isCycle = true
                } else {
                    add(next)
                    v = next
                }
            }
        }
    }
//    println(nonCycleMap)
//    println(cycleMap)
    val answer = initVideoList.map { init ->
        var cycleCnt = M - 1
        var temp = init
        val nonCycle = nonCycleMap[init]
        while ((nonCycle?.isNotEmpty() == true) && cycleCnt > 0) {
            temp = nonCycle.poll()
            cycleCnt--
        }
        val cycle = cycleMap[init]
        if (cycleCnt > 0 && cycle?.isNotEmpty() == true) {
            val idx = (cycleCnt % cycle.size).toInt()
            if (idx >= 1) {
                temp = cycle[idx - 1]
            }
        }
        temp
    }
    println(answer.joinToString(" "))
}
