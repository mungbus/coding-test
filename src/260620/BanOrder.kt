package `260620`

class Solution {
    fun solution(n: Long, bans: Array<String>): String {
        // 1. bans 배열의 문자열들을 고유 번호(Long)로 변환 후 오름차순 정렬
        val banNumbers = bans.map { stringToOrder(it) }.sorted()

        // 2. target 번호를 n으로 시작하여, 정렬된 금지어 번호들을 보며 뒤로 밀어내기
        var target = n
        for (ban in banNumbers) {
            // 금지어 번호가 현재 target 이하에 있다면 실제 주문이 뒤로 한 칸 밀려남
            if (ban <= target) {
                target++
            } else {
                // 금지어 번호가 target보다 커지면 이후의 금지어들은 영향을 주지 않으므로 탈출
                break
            }
        }

        // 3. 최종 보정된 target 번호를 다시 문자열 주문으로 복원하여 반환
        return orderToString(target)
    }

    /**
     * [문자열 -> 고유 번호 변환 함수]
     * 엑셀 열 번호 방식(A=1, AA=27)의 누적 법칙을 fold로 구현.
     * 수식: (이전까지의 누적값 * 26) + (현재 알파벳의 1-index 순서)
     * 예시 'aa':
     * - 'a' 처리: 0 * 26 + 1 = 1
     * - 'a' 처리: 1 * 26 + 1 = 27
     */
    private fun stringToOrder(s: String): Long =
        s.fold(0L) { acc, char -> acc * 26 + (char - 'a' + 1) }

    /**
     * [고유 번호 -> 문자열 변환 함수]
     * 시퀀스(generateSequence)를 활용하여 숫자가 0이 될 때까지 역산하며 자릿수를 추출.
     * 1. generateSequence로 매 단계마다 (it - 1) / 26을 수행하며 이전 자릿수의 값 추적
     * 2. map { (it - 1) % 26 }을 통해 현재 자릿수의 26진법 나머지(0~25) 계산
     * 3. 나머지를 알파벳 문자('a' + 남은값)로 매칭 후 문자열 변환
     * 4. 역순으로 생성된 리스트를 뒤집고(reversed) 하나의 문자열로 결합
     */
    private fun orderToString(n: Long): String =
        generateSequence(n) { if (it > 26) (it - 1) / 26 else null }
            .map { (it - 1) % 26 }
            .map { ('a' + it.toInt()).toString() }
            .toList()
            .reversed()
            .joinToString("")
}
