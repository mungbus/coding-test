package `251108`

fun main() {
    var caseNo = 1
    while (true) {
        val line = readln()
        val tokens = line.split(" ")
        val nums = tokens.map { it.toLong() }
        if (nums.all { it == 0L }) return
        val results = mutableSetOf<Long>()
        val visited = mutableSetOf<String>()

        fun dfs(list: List<Long>) {
            if (list.size == 1) {
                results.add(list.first())
                return
            }
            val key = list.sorted().joinToString(",")
            if (!visited.add(key)) return

            val n = list.size
            for (i in 0 until n) {
                for (j in i + 1 until n) {
                    val a = list[i]
                    val b = list[j]
                    val rem = mutableListOf<Long>()
                    for (k in 0 until n) if (k != i && k != j) rem.add(list[k])

                    // a + b
                    run {
                        val next = ArrayList(rem)
                        next.add(a + b)
                        dfs(next)
                    }
                    // a * b
                    run {
                        val next = ArrayList(rem)
                        next.add(a * b)
                        dfs(next)
                    }
                    // a - b
                    run {
                        val next = ArrayList(rem)
                        next.add(a - b)
                        dfs(next)
                    }
                    // b - a
                    run {
                        val next = ArrayList(rem)
                        next.add(b - a)
                        dfs(next)
                    }
                    // a / b if divisible
                    if (b != 0L && a % b == 0L) {
                        val next = ArrayList(rem)
                        next.add(a / b)
                        dfs(next)
                    }
                    // b / a if divisible
                    if (a != 0L && b % a == 0L) {
                        val next = ArrayList(rem)
                        next.add(b / a)
                        dfs(next)
                    }
                }
            }
        }

        dfs(nums)

        val sorted = results.toMutableList().sorted()
        var bestStart = sorted.first()
        var bestEnd = sorted.first()
        var curStart = sorted.first()
        var curPrev = sorted.first()
        for (i in 1 until sorted.size) {
            val v = sorted[i]
            if (v == curPrev + 1) {
                curPrev = v
            } else {
                val curLen = (curPrev - curStart) + 1
                val bestLen = (bestEnd - bestStart) + 1
                if (curLen > bestLen || (curLen == bestLen && curStart > bestStart)) {
                    bestStart = curStart
                    bestEnd = curPrev
                }
                curStart = v
                curPrev = v
            }
        }

        val curLen = (curPrev - curStart) + 1
        val bestLen = (bestEnd - bestStart) + 1
        if (curLen > bestLen || (curLen == bestLen && curStart > bestStart)) {
            bestStart = curStart
            bestEnd = curPrev
        }

        println("Case $caseNo: $bestStart to $bestEnd")
        caseNo++
    }
}
