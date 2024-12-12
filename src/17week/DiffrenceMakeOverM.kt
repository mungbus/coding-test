package `17week`

fun main() {
    val (N, M) = readln().split(" ").map { it.toInt() }
    val array = readln().split(" ").map { it.toInt() }

    // 절대값이므로 방향은 중요하지 않음, 크기만 차이나면 됨 <- 이게 가장 핵심
    // 그래서 한칸씩 띄면서 체크하는게 중요함 (일반적으로)
    // 처음엔 현재 0번, 두번째는 1번에 체크 (값이 변경이 필요한 시점을 기준으로)
}
