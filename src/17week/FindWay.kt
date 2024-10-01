package `17week`

fun main() {
    val N = readln().toInt()
    val adjacencyMatrix = Array(N) {
        Array(N) { 0 }
    }
    val ijList: LinkedHashSet<Pair<Int, Int>> = LinkedHashSet<Pair<Int, Int>>()
    repeat(N) { i ->
        readln().split(" ").forEachIndexed { j, it ->
            if (it.toInt() >= 1) {
                fun LinkedHashSet<Pair<Int, Int>>.connected(): Set<Pair<Int, Int>> {
                    val result = flatMap { (befI, befJ) ->
                        val tempList = LinkedHashSet<Pair<Int, Int>>()
                        if (befJ == i) {
                            adjacencyMatrix[befI][j] = 1
                            tempList.add(befI to j)
                        }
                        if (befI == j) {
                            adjacencyMatrix[i][befJ] = 1
                            tempList.add(i to befJ)
                        }
                        tempList
                    }.toSet()
                    val check = this.addAll(result)
                    return if (check) {
                        this.connected()
                    } else {
                        this
                    }
                }
                ijList.add(i to j)
                adjacencyMatrix[i][j] = 1
                ijList.connected()
            }
        }
    }
    println(adjacencyMatrix.map {
        it.joinToString(" ")
    }.joinToString("\n"))
}
