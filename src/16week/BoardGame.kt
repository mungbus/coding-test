package `16week`

import java.util.Scanner

fun main() {
    val sc = Scanner(System.`in`)
    val N = sc.nextInt()
    val M = sc.nextInt()

    // Alice의 카드 배치를 저장
    val aliceCards = Array(N) { CharArray(M) }
    for (i in 0 until N) {
        aliceCards[i] = sc.next().toCharArray()
    }

    // Bob의 카드 배치를 저장
    val bobCards = Array(N) { CharArray(M) }
    for (i in 0 until N) {
        bobCards[i] = sc.next().toCharArray()
    }

    // 교환 횟수 Q
    val Q = sc.nextInt()

    // 교환 명령들을 저장
    val exchanges = mutableListOf<Quadruple>()
    for (i in 0 until Q) {
        val r1 = sc.nextInt() - 1
        val c1 = sc.nextInt() - 1
        val r2 = sc.nextInt() - 1
        val c2 = sc.nextInt() - 1
        exchanges.add(Quadruple(r1, c1, r2, c2))
    }

    // 현재 상태에서 각 열의 첫 번째 카드를 추출
    val aliceTopCards = CharArray(M) { col -> aliceCards[0][col] }
    val bobTopCards = CharArray(M) { col -> bobCards[0][col] }

    // 현재 상태에서 승리자를 결정하는 함수
    fun determineWinner(): String {
        var currentPlayer = "Alice"

        // 각 턴마다 Alice 또는 Bob이 카드를 선택
        while (true) {
            if (currentPlayer == "Alice") {
                // Alice가 A가 있는 열을 찾으면 계속 Alice가 진행
                for (col in 0 until M) {
                    if (aliceTopCards[col] != 'X') {  // 'X'는 이미 제거된 카드
                        if (aliceTopCards[col] == 'A') {
                            aliceTopCards[col] = 'X'
                            currentPlayer = "Alice"
                        } else {
                            aliceTopCards[col] = 'X'
                            currentPlayer = "Bob"
                        }
                        break
                    }
                }
                if (aliceTopCards.all { it == 'X' }) return "Bob"
            } else if (currentPlayer == "Bob") {
                // Bob이 B가 있는 열을 찾으면 계속 Bob이 진행
                for (col in 0 until M) {
                    if (bobTopCards[col] != 'X') {
                        if (bobTopCards[col] == 'B') {
                            bobTopCards[col] = 'X'
                            currentPlayer = "Bob"
                        } else {
                            bobTopCards[col] = 'X'
                            currentPlayer = "Alice"
                        }
                        break
                    }
                }
                if (bobTopCards.all { it == 'X' }) return "Alice"
            }
        }
    }

    // 초기 상태에서 승리자 출력
    println(determineWinner())

    // 교환마다 승리자 출력
    for (exchange in exchanges) {
        // 교환 수행
        val (r1, c1, r2, c2) = exchange
        val temp = aliceCards[r1][c1]
        aliceCards[r1][c1] = bobCards[r2][c2]
        bobCards[r2][c2] = temp

        // 다시 첫 번째 카드를 업데이트
        for (col in 0 until M) {
            aliceTopCards[col] = aliceCards[0][col]
            bobTopCards[col] = bobCards[0][col]
        }

        // 교환 후 승리자 출력
        println(determineWinner())
    }
}

// 데이터 클래스: 네 가지 값을 저장할 수 있는 구조체
data class Quadruple(val r1: Int, val c1: Int, val r2: Int, val c2: Int)
