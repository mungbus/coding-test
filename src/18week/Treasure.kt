package `18week`

fun main() {
    val (R) = readln().split(" ").map { it.toInt() }
    val map = Array(R) {
        readln().split("")
    }

    // 맵 내에서 최단거리 가장 긴 곳의 길이 = 보물의 위치, 보물의 위치가 아닌 가장 긴 거리를 찾아라 = 모든 꼭지점을 찾아라
}
