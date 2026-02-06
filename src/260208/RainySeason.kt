package `260208`

fun main() {
    // 1. 입력 데이터를 리스트로 변환 (공백 제거 포함)
    val (N, M, Q) = readln().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

    // a: 매일 새로 들어오는 상자의 초기 빗물 양
    val a = readln().split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    // 누적합 및 데이터 저장용 배열
    val sumA = LongArray(N + 1)           // t일까지 내린 총 강수량 누적합
    val dailyEvap = LongArray(N + 1)      // t일 하루 동안 발생하는 총 증발량
    val totalEvapSum = LongArray(N + 1)   // t일까지 발생한 총 증발량의 누적합

    // 효율적인 계산을 위한 배열들
    val activeCountDiff = IntArray(N + 2) // 특정 날에 '증발 가능한 물이 있는 상자' 수 (차분 배열)
    val lastEvap = LongArray(N + 1)       // 상자의 물이 M보다 적게 남아 마지막으로 증발하는 양
    val boxesEndingToday = IntArray(N + 1) // 특정 날에 물이 완전히 다 말라버리는 상자의 개수

    // 2. 각 상자마다 "언제 들어오고 언제 마르는지"를 미리 계산 (Preprocessing)
    a.forEachIndexed { index, water ->
        val day = index + 1 // 상자가 들어온 날 (1일차, 2일차...)
        sumA[day] = sumA[day - 1] + water // 지금까지 온 총 강수량 저장

        // 문제 조건: 보관된 상자는 '다음날'부터 증발을 시작함
        if (day < N) {
            val startEvapDay = day + 1 // 증발 시작일
            // daysLasting: M만큼 꽉 채워서 증발할 수 있는 기간 (0일부터 시작)
            val daysLasting = (water - 1) / M
            val endDay = (startEvapDay + daysLasting).toInt() // 물이 다 마르는 날

            // [차분 배열 활용] startEvapDay부터 물이 있는 상자 개수 증가
            activeCountDiff[startEvapDay]++

            if (endDay <= N) {
                // endDay에 물이 다 마르므로, endDay+1일부터는 개수에서 제외
                activeCountDiff[endDay + 1]--
                // 오늘(endDay)이 이 상자의 마지막 증발일임을 기록
                boxesEndingToday[endDay]++
                // 마지막 날 증발량 = 원래 양 - (전날까지 M씩 증발했던 양의 합)
                lastEvap[endDay] += water - (M.toLong() * (endDay - startEvapDay))
            }
        }
    }

    // 3. 1일부터 N일까지 매일매일의 증발 데이터를 합산
    var currentActiveBoxes = 0L // 오늘 물이 남아있어서 증발이 일어나는 상자 수
    repeat(N) { i ->
        val t = i + 1
        // 오늘 새롭게 추가되거나 끝난 상자 정보를 바탕으로 현재 활성 상자 수 갱신
        currentActiveBoxes += activeCountDiff[t]

        // 당일 증발량 계산 logic:
        // (오늘 물이 다 마르지 않는 상자들은 M만큼 증발) + (오늘 딱 마르는 상자들의 잔여 물 증발)
        dailyEvap[t] = (currentActiveBoxes - boxesEndingToday[t]) * M + lastEvap[t]

        // 질문 1에 답하기 위해 매일의 증발량을 계속 누적해둠
        totalEvapSum[t] = totalEvapSum[t - 1] + dailyEvap[t]
    }

    // 4. 교수님의 질문(Q)에 대한 답변 출력
    val answer = StringBuilder()
    repeat(Q) {
        val query = readln().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val (type, t) = query

        if (type == 1) {
            // 질문 1: 모든 상자에 남은 물의 총합 = (총 강수량) - (총 증발량)
            answer.append("${sumA[t] - totalEvapSum[t]}\n")
        } else {
            // 질문 2: 오늘 하루 동안 증발한 물의 총량
            answer.append("${dailyEvap[t]}\n")
        }
    }

    // 모든 답변을 모아서 한 번에 출력 (성능 최적화)
    print(answer)
}
