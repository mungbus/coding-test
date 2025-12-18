package `251221`

fun main() {
    val t = readln().toInt() // 테스트 케이스 개수

    repeat(t) {
        val n = readln().toInt() // 노드의 수
        val parent = IntArray(n + 1) { 0 } // 부모 노드 저장 배열

        // 간선 정보 입력 (A가 B의 부모)
        repeat(n - 1) {
            val (a, b) = readln().split(" ").map { it.toInt() }
            parent[b] = a
        }

        // 공통 조상을 찾을 두 노드
        val (u, v) = readln().split(" ").map { it.toInt() }

        // 1. 첫 번째 노드(u)의 모든 조상을 찾아 기록 (방문 체크)
        val visited = BooleanArray(n + 1) { false }
        var curr = u
        while (curr != 0) {
            visited[curr] = true
            curr = parent[curr] // 루트까지 거슬러 올라감
        }

        // 2. 두 번째 노드(v)에서 루트로 올라가며 처음으로 방문된 노드 찾기
        curr = v
        while (curr != 0) {
            if (visited[curr]) {
                println(curr) // 가장 먼저 만나는 공통 조상 출력
                break
            }
            curr = parent[curr]
        }
    }
}
