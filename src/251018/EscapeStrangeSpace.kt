package `251018`

import java.util.LinkedList
import java.util.Queue

data class Point3D(val z: Int, val x: Int, val y: Int)
data class State(val p: Point3D, val t: Int)
data class Anomaly(val r: Int, val c: Int, val d: Int, val v: Int)

val directions = listOf(
    Point3D(0, 0, 1),  // 0: 동
    Point3D(0, 0, -1), // 1: 서
    Point3D(0, 1, 0),  // 2: 남
    Point3D(0, -1, 0), // 3: 북
    Point3D(1, 0, 0), // up
    Point3D(-1, 0, 0) // down
)

fun main() {
    val br = System.`in`.bufferedReader()
    val (N, M, F) = br.readLine().split(" ").map { it.toInt() }

    val floorMap = Array(N) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }

    // 문제의 입력 순서: 동, 서, 남, 북, 윗면
    // 하지만 제 코드에서는 top, north, south, east, west 순서로 읽고 있었습니다.
    // 이 순서에 맞게 입력을 다시 정렬합니다.
    val wallEastData = Array(M) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }
    val wallWestData = Array(M) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }
    val wallSouthData = Array(M) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }
    val wallNorthData = Array(M) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }
    val wallTop = Array(M) { br.readLine().split(" ").map { it.toInt() }.toIntArray() }

    // 제 코드의 내부 구현 순서(top, north, south, east, west)에 맞게 재할당합니다.
    val wallNorth = wallNorthData
    val wallSouth = wallSouthData
    val wallEast = wallEastData
    val wallWest = wallWestData


    val anomalies = Array(F) {
        val (r, c, d, v) = br.readLine().split(" ").map { it.toInt() }
        Anomaly(r - 1, c - 1, d, v)
    }

    // 3D map: z, x, y. z=0 is floor, z=1..M is wall
    val space = Array(M + 1) { Array(N) { IntArray(N) } }
    var startPos: Point3D? = null
    var exitPos: Point3D? = null
    var wallPos: Point3D? = null

    for (i in 0 until N) {
        for (j in 0 until N) {
            space[0][i][j] = if (floorMap[i][j] == 1) 1 else 0
            if (floorMap[i][j] == 3) {
                wallPos = Point3D(0, i, j)
            }
            if (floorMap[i][j] == 4) {
                exitPos = Point3D(0, i, j)
            }
        }
    }

    if (wallPos == null || exitPos == null) {
        println(-1)
        return
    }

    val wx = wallPos.x
    val wy = wallPos.y

    // Build 3D wall
    for (z_rel in 0 until M) { // z_rel from bottom (0) to top (M-1)
        for (x_rel in 0 until M) {
            for (y_rel in 0 until M) {
                val absZ = z_rel + 1
                val absX = wx + x_rel
                val absY = wy + y_rel

                if (absX >= N || absY >= N) continue

                val z_view_idx = M - 1 - z_rel
                val isObstacle = wallTop[x_rel][y_rel] == 1 ||
                        wallNorth[z_view_idx][y_rel] == 1 ||
                        wallSouth[z_view_idx][y_rel] == 1 ||
                        wallEast[z_view_idx][x_rel] == 1 ||
                        wallWest[z_view_idx][x_rel] == 1

                if (isObstacle) {
                    space[absZ][absX][absY] = 1
                } else {
                    space[absZ][absX][absY] = 0
                    if (wallTop[x_rel][y_rel] == 2) {
                        startPos = Point3D(absZ, absX, absY)
                    }
                }
            }
        }
    }

    // The floor under the wall is an obstacle, except for one opening.
    var passageFound = false
    loop@ for (x_rel in 0 until M) {
        for (y_rel in 0 until M) {
            val absX = wx + x_rel
            val absY = wy + y_rel
            if (absX >= N || absY >= N) continue

            if (!passageFound && space[1][absX][absY] == 0 && floorMap[absX][absY] == 0) {
                // This is the single passage. Keep it open.
                passageFound = true
            } else {
                space[0][absX][absY] = 1 // Block other floor cells under the wall.
            }
        }
    }
    if (passageFound) {
        loop@ for (x_rel in 0 until M) {
            for (y_rel in 0 until M) {
                val absX = wx + x_rel
                val absY = wy + y_rel
                if (absX >= N || absY >= N) continue
                if (space[1][absX][absY] == 0 && floorMap[absX][absY] == 0) {
                    space[0][absX][absY] = 0
                    break@loop
                }
            }
        }
    }


    // anomalyMap[r][c] = the first time a cell becomes an obstacle
    val anomalyMap = Array(N) { IntArray(N) { Int.MAX_VALUE } }
    val anomalyDirs = listOf(
        Point3D(0, 0, 1),  // 0: 동
        Point3D(0, 0, -1), // 1: 서
        Point3D(0, 1, 0),  // 2: 남
        Point3D(0, -1, 0)  // 3: 북
    )

    for (anomaly in anomalies) {
        var r = anomaly.r
        var c = anomaly.c
        val dir = anomalyDirs[anomaly.d]
        var time = 0

        while (r in 0 until N && c in 0 until N && space[0][r][c] == 0 && !(r == exitPos.x && c == exitPos.y)) {
            anomalyMap[r][c] = minOf(anomalyMap[r][c], time)
            time += anomaly.v
            r += dir.x
            c += dir.y
        }
    }

    if (startPos == null) {
        println(-1)
        return
    }

    val q: Queue<State> = LinkedList()
    val visited = Array(M + 1) { Array(N) { IntArray(N) { -1 } } }

    q.add(State(startPos, 0))
    visited[startPos.z][startPos.x][startPos.y] = 0

    var answer = -1

    while (q.isNotEmpty()) {
        val (p, t) = q.poll()

        if (p.z == exitPos.z && p.x == exitPos.x && p.y == exitPos.y) {
            answer = t
            break
        }

        for (dir in directions) {
            val nz = p.z + dir.z
            val nx = p.x + dir.x
            val ny = p.y + dir.y
            val nt = t + 1

            if (nz !in 0..M || nx !in 0 until N || ny !in 0 until N) continue
            if (space[nz][nx][ny] == 1) continue

            if (nz == 0) {
                if (nt >= anomalyMap[nx][ny]) continue
            }

            visited[nz][nx][ny] = nt
            q.add(State(Point3D(nz, nx, ny), nt))
        }
    }
    println(answer)
}
