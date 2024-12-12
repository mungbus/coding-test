package `16week`

fun main() {
    val N = readln().toInt()
    val adjacencyMatrix = Array(N) { Array(N) { 0 } }

    fun LinkedHashSet<Pair<Int, Int>>.bfsUpdate(i: Int, j: Int) {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.addLast(i to j)
        while (queue.isNotEmpty()) {
            val (curI, curJ) = queue.removeFirst()

            forEach { (befI, befJ) ->
                if (befJ == curI && adjacencyMatrix[befI][curJ] == 0) {
                    adjacencyMatrix[befI][curJ] = 1
                    queue.addLast(befI to curJ)
                }
                if (befI == curJ && adjacencyMatrix[curI][befJ] == 0) {
                    adjacencyMatrix[curI][befJ] = 1
                    queue.addLast(curI to befJ)
                }
            }
        }
    }

    val ijList: LinkedHashSet<Pair<Int, Int>> = LinkedHashSet()
    repeat(N) { i ->
        readln().split(" ").forEachIndexed { j, it ->
            if (it.toInt() >= 1) {
                ijList.add(i to j)
                adjacencyMatrix[i][j] = 1
                ijList.bfsUpdate(i, j)
            }
        }
    }

    println(adjacencyMatrix.joinToString("\n") { it.joinToString(" ") })
}
