package `16week`

fun main() {
    val N = readln().toInt()
    val adjacencyMatrix = Array(N) {
        Array(N) { 0 }
    }
    val visited = Array(N) { BooleanArray(N) { false } }

    fun LinkedHashSet<Pair<Int, Int>>.connections(i: Int, j: Int) {
        if (visited[i][j]) return
        visited[i][j] = true

        val result = flatMap { (befI, befJ) ->
            val tempList = LinkedHashSet<Pair<Int, Int>>()
            if (befJ == i && adjacencyMatrix[befI][j] == 0) {
                adjacencyMatrix[befI][j] = 1
                tempList.add(befI to j)
            }
            if (befI == j && adjacencyMatrix[i][befJ] == 0) {
                adjacencyMatrix[i][befJ] = 1
                tempList.add(i to befJ)
            }
            tempList
        }
        if (result.isNotEmpty()) {
            addAll(result)
            result.forEach { (newI, newJ) ->
                connections(newI, newJ)
            }
        }
    }

    val ijList: LinkedHashSet<Pair<Int, Int>> = LinkedHashSet()
    repeat(N) { i ->
        readln().split(" ").forEachIndexed { j, it ->
            if (it.toInt() >= 1) {
                ijList.add(i to j)
                adjacencyMatrix[i][j] = 1
                ijList.connections(i, j)
            }
        }
    }
    println(adjacencyMatrix.map {
        it.joinToString(" ")
    }.joinToString("\n"))
}
