package `260412`

import java.util.Scanner

fun main() {
    val sc = Scanner(System.`in`)

    // 길이 N 입력
    if (!sc.hasNextInt()) return
    val n = sc.nextInt()

    // 1. 기본 충돌 쌍 "Aa"와 "BB"를 준비합니다.
    // 2. 길이가 N이 되어야 하므로 나머지 (N-2)만큼 동일한 문자를 뒤에 붙여줍니다.
    // 3. Scanner.next()로 읽을 수 있어야 하므로 공백이나 특수문자가 없는 문자를 사용합니다.

    val padding = "a".repeat(n - 2)

    val a = "Aa$padding"
    val b = "BB$padding"

    // 결과 출력
    println(a)
    println(b)
}
