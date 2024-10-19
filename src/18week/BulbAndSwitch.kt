package `18week`

fun main() {
    val N = readln().toInt()
    val before = readln().split("").map { it.toInt() }
    val after = readln().split("").map { it.toInt() }

    // 누를 곳 눌러서 상태값을 저장해야 하는데 이 상태값을 최적으로 하는 경우로 계산
    // 1번 예시 110 -> 101 -> 010 방식 순
    // 전후 값을 바꿔주는 버튼을 클릭하고 같이 뒤집어주면 끝
}
