package `260403`

class Edge(val to: Int, val weight: Int)

fun main() {
    val tString = readlnOrNull() ?: return
    val t = tString.toInt()

    repeat(t) {
        val line = readlnOrNull() ?: return@repeat
        val (n, m) = line.split(" ").map { it.toInt() }

        val adj = Array(n + 1) { mutableListOf<Edge>() }

        repeat(m) {
            val edgeLine = readlnOrNull() ?: return@repeat
            val (u, v, w) = edgeLine.split(" ").map { it.toInt() }
            adj[u].add(Edge(v, w))
            adj[v].add(Edge(u, w))
        }

        // 섬이 1개뿐이면 폭파할 다리가 없음
        if (n <= 1) {
            println(0)
        } else {
            println(dfs(1, 0, adj, 0))
        }
    }
}

/**
 * u: 현재 섬 번호, p: 부모 섬 번호, adj: 인접 리스트, costToParent: 부모와 연결된 다리 비용
 */
fun dfs(u: Int, p: Int, adj: Array<MutableList<Edge>>, costToParent: Int): Long {
    // 리프 노드 판정: 1번 섬이 아니고, 연결된 다리가 부모와 연결된 1개뿐인 경우
    if (u != 1 && adj[u].size == 1) {
        return costToParent.toLong()
    }

    var childSum = 0L
    for (edge in adj[u]) {
        if (edge.to == p) continue
        childSum += dfs(edge.to, u, adj, edge.weight)
    }

    // 1번 섬(진의 위치)인 경우, 하위 노드들을 차단한 비용의 합을 그대로 반환
    if (u == 1) return childSum

    // 현재 다리를 끊는 비용(costToParent)과
    // 하위 리프 노드들을 모두 차단해온 비용(childSum) 중 최솟값 선택
    return minOf(costToParent.toLong(), childSum)
}
