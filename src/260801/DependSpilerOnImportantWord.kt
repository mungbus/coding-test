package `260801`

class Solution {
    data class Word(
        val text: String,
        val startIndex: Int,
        val endIndex: Int
    ) {
        // 단어 범위와 스포 방지 구간이 겹치는지 여부를 판단하는 확장 함수
        fun overlapsWith(range: IntArray): Int {
            val (start, end) = range
            return if (startIndex <= end && endIndex >= start) 1 else 0
        }
    }

    fun solution(message: String, spoiler_ranges: Array<IntArray>): Int {
        // 1. 메시지를 단어 객체 리스트로 변환
        var currentIndex = 0
        val words = message.split(" ").map { raw ->
            val start = currentIndex
            val end = start + raw.length - 1
            currentIndex = end + 2
            Word(raw, start, end)
        }

        // 2. 어떤 스포 방지 구간에도 속하지 않은 '일반 영역'에 등장한 단어들 세트
        // (단어의 문자가 스포 구간 중 어디에도 포함되지 않는 경우)
        val normalAreaWords = words.filter { word ->
            spoiler_ranges.none { range -> word.overlapsWith(range) > 0 }
        }.map { it.text }.toSet()

        // 3. 스포 방지 구간별로 포함된 단어들을 순서대로 추출
        val spoilerWordsByIndex = spoiler_ranges.map { range ->
            words.filter { word -> word.overlapsWith(range) > 0 }.map { it.text }
        }

        // 4. 스포 방지 구간을 순서대로 열면서 중요한 단어 카운트
        val revealedWords = mutableSetOf<String>()
        var answer = 0

        spoilerWordsByIndex.forEach { wordList ->
            wordList.forEach { word ->
                if (word !in normalAreaWords && revealedWords.add(word)) {
                    answer++
                }
            }
        }

        return answer
    }
}

fun main() {
    val solution = Solution()

    // 테스트 케이스 1
    val message1 = "here is muzi here is a secret message"
    val ranges1 = arrayOf(
        intArrayOf(0, 3),
        intArrayOf(23, 28)
    )
    val result1 = solution.solution(message1, ranges1)
    println("Test 1 Result: $result1 (Expected: 1)")

    // 테스트 케이스 2
    val message2 = "my phone number is 01012345678 and may i have your phone number"
    val ranges2 = arrayOf(
        intArrayOf(5, 5),
        intArrayOf(25, 28),
        intArrayOf(34, 40),
        intArrayOf(53, 59)
    )
    val result2 = solution.solution(message2, ranges2)
    println("Test 2 Result: $result2 (Expected: 4)")
}
