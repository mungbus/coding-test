package `260208`

import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

// 좌표를 저장할 데이터 클래스
data class Point(val x: Int, val y: Int)

fun main() {
    // 테스트 케이스 개수 입력
    val t = readln().toInt()

    repeat(t) {
        // 편의점 개수 입력
        val n = readln().toInt()
        val stores = ArrayList<Point>()

        // 1. 상근이네 집 좌표 (출발점)
        val (x, y) = readln().split(" ").map { it.toInt() }
        val start = Point(x, y)

        // 2. 편의점 좌표들
        repeat(n) {
            val (x, y) = readln().split(" ").map { it.toInt() }
            stores.add(Point(x, y))
        }

        // 3. 펜타포트 락 페스티벌 좌표 (도착점)
        val (endX, endY) = readln().split(" ").map { it.toInt() }
        val end = Point(endX, endY)

        // BFS 탐색 실행 및 결과 출력
        if (bfs(start, stores, end)) {
            println("happy")
        } else {
            println("sad")
        }
    }
}

fun bfs(start: Point, stores: ArrayList<Point>, end: Point): Boolean {
    val q: Queue<Point> = LinkedList()
    val visited = BooleanArray(stores.size) // 편의점 방문 여부 체크

    q.add(start)

    while (q.isNotEmpty()) {
        val current = q.poll()

        // 현재 위치에서 바로 페스티벌 장소로 갈 수 있는지 확인 (1000m 이내)
        if (getDistance(current, end) <= 1000) {
            return true
        }

        // 갈 수 있는 편의점 탐색
        for (i in stores.indices) {
            if (!visited[i]) {
                val nextStore = stores[i]
                // 현재 위치에서 해당 편의점까지 거리가 1000m 이내라면 이동 가능
                if (getDistance(current, nextStore) <= 1000) {
                    visited[i] = true
                    q.add(nextStore)
                }
            }
        }
    }

    return false
}

// 맨해튼 거리 계산 함수
fun getDistance(p1: Point, p2: Point): Int {
    return abs(p1.x - p2.x) + abs(p1.y - p2.y)
}
