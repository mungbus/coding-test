package `250830`

class DiameterOfTree(val n: Int, val graph: Array<MutableList<Pair<Int, Int>>>) {
    fun bfs(start: Int): Pair<Int, Int> {
        val queue = ArrayDeque<Int>()
        val distances = IntArray(n) { -1 }
        queue.add(start)
        distances[start] = 0

        var farNode = start
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            graph[node].forEach { (child, weight) ->
                if (distances[child] == -1) {
                    distances[child] = distances[node] + weight
                    queue.add(child)
                    if (distances[child] > distances[farNode]) {
                        farNode = child
                    }
                }
            }
        }
        return Pair(farNode, distances[farNode])
    }
}

fun main() {
    val n = readln().toInt()
    val edges = List(n - 1) {
        val (parent, child, weight) = readln().split(" ").map { it.toInt() }
        Triple(parent - 1, child - 1, weight)
    }

    val graph = Array(n) { mutableListOf<Pair<Int, Int>>() }
    edges.forEach { (parent, child, weight) ->
        graph[parent].add(child to weight)
        graph[child].add(parent to weight)
    }

    val diameterOfTree = DiameterOfTree(n, graph)
    val (farNode, _) = diameterOfTree.bfs(0)
    val (_, diameter) = diameterOfTree.bfs(farNode)

    println(diameter)
}
