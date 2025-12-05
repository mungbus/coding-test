package `251207`

import java.util.ArrayDeque

private data class Node(val r: Int, val c: Int, val virus: Int, val time: Int)

fun main() {
    // 입력: n(행), m(열)
    val (n, m) = readln().split(" ").map { it.toInt() }

    // 각 칸의 현재 상태 (0:빈칸, 1:바이러스1, 2:바이러스2, 3:바이러스3, -1:치료제)
    val state = Array(n) { IntArray(m) }
    // 각 칸이 완전히 감염되는 시간 (-1:미감염)
    val finishTime = Array(n) { IntArray(m) { -1 } }
    // 다중 시작점 BFS를 위해 모든 초기 바이러스를 담음
    val queue = ArrayDeque<Node>()

    // 지도 입력 및 초기 바이러스 위치를 큐에 추가
    repeat(n) { r ->
        val row = readln().split(" ").map { it.toInt() }
        repeat(m) { c ->
            val value = row[c]
            state[r][c] = value
            // 1번 또는 2번 바이러스 초기 위치는 시간 0으로 큐에 추가
            if (value == 1 || value == 2) {
                finishTime[r][c] = 0
                queue.addLast(Node(r, c, value, 0))
            }
        }
    }

    val directions = listOf(
        Pair(-1, 0), // 상
        Pair(1, 0),  // 하
        Pair(0, -1), // 좌
        Pair(0, 1)   // 우
    )

    // BFS 시작: 모든 바이러스가 동시에 퍼져나감
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        // 현재 위치가 이미 다른 바이러스에 감염되었으면 스킵
        if (state[current.r][current.c] != current.virus) continue
        // 현재 위치의 감염 완료 시간이 맞지 않으면 스킵 (중복 처리 방지)
        if (finishTime[current.r][current.c] != current.time) continue

        // 상하좌우 4방향 탐색
        directions.forEach { (dr, dc) ->
            val nr = current.r + dr
            val nc = current.c + dc

            // 경계를 벗어나면 스킵
            if (nr !in 0 until n || nc !in 0 until m) return@forEach
            val cellState = state[nr][nc]
            // 치료제가 있거나 3번 바이러스면 전파 불가
            if (cellState == -1 || cellState == 3) return@forEach

            val nextTime = current.time + 1

            if (cellState == 0) {
                // 빈 칸이면 현재 바이러스로 감염
                state[nr][nc] = current.virus
                finishTime[nr][nc] = nextTime
                queue.addLast(Node(nr, nc, current.virus, nextTime))
            } else if (cellState != current.virus
                && finishTime[nr][nc] == nextTime
                && setOf(1, 2).contains(cellState)
            ) {
                // 다른 바이러스가 같은 시간에 도착하면 3번 바이러스 생성
                // 3번 바이러스는 더 이상 전파되지 않으므로 큐에 추가하지 않음
                state[nr][nc] = 3
                finishTime[nr][nc] = nextTime
            }
        }
    }

    var count1 = 0
    var count2 = 0
    var count3 = 0

    repeat(n) { r ->
        repeat(m) { c ->
            when (state[r][c]) {
                1 -> count1++
                2 -> count2++
                3 -> count3++
            }
        }
    }

    println("$count1 $count2 $count3")
}
