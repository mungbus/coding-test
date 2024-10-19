package `18week`

fun main() {
    val (N, K) = readln().split(" ").map { it.toInt() }
    val table = readln().split("")

    // 모든 경우에 대한 햄버거 가능 여부 체크 -> DFS로 최대 체크 필요
    // 사람과 햄버거를 두고 각 사람이 가능한 버거를 번호로 둔다
    // 현재 버거중에 제거해 가면서 조건을 테스트
}
