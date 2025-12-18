package `251207`

import kotlin.math.sqrt

data class Edge(val u: Int, val v: Int, val weight: Double) : Comparable<Edge> {
    override fun compareTo(other: Edge) = this.weight.compareTo(other.weight)
}

class UnionFind(val n: Int) {
    private val parent = IntArray(n) { it }
    private val rank = IntArray(n)

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x])
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)
        if (rootX == rootY) return false

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX
        } else {
            parent[rootY] = rootX
            rank[rootX]++
        }
        return true
    }
}

fun distance(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Double {
    val dx = p1.first - p2.first
    val dy = p1.second - p2.second
    return sqrt((dx * dx + dy * dy).toDouble())
}

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }

    // 우주신들의 좌표 입력
    val points = (0 until N).map {
        val (x, y) = readln().split(" ").map { it.toLong() }
        x to y
    }

    // 모든 가능한 간선 생성
    val edges = (0 until N).flatMap { i ->
        (i + 1 until N).map { j ->
            Edge(i, j, distance(points[i], points[j]))
        }
    }.sorted()

    // UnionFind 초기화
    val uf = UnionFind(N)

    // 이미 연결된 간선들을 먼저 UnionFind에 등록
    repeat(M) {
        val (u, v) = readln().split(" ").map { it.toInt() - 1 }
        uf.union(u, v)
    }

    // Kruskal 알고리즘으로 MST 구성 (새로 추가되는 간선만)
    val result = edges.filter { uf.union(it.u, it.v) }.sumOf { it.weight }

    println(String.format("%.2f", result))
}
