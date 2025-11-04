package `251108`

fun main() {
    val N = readln().toInt()
    val M = readln().toInt()
    val vipSeats = IntArray(M) {
        readln().toInt()
    }.also { it.sort() }

    // 각 VIP 사이(및 양 끝)의 구간 길이 중 최대값 계산
    var prev = 0
    var maxLen = 0
    for (vip in vipSeats) {
        val len = vip - prev - 1
        if (len > maxLen) maxLen = len
        prev = vip
    }
    val tail = N - prev
    if (tail > maxLen) maxLen = tail

    // 최대 길이만큼만 ways 계산
    val ways = LongArray(maxLen + 1)
    ways[0] = 1L
    if (maxLen >= 1) ways[1] = 1L
    for (i in 2..maxLen) ways[i] = ways[i - 1] + ways[i - 2]

    // 구간별로 곱셈 수행
    var ans = 1L
    prev = 0
    for (vip in vipSeats) {
        val len = vip - prev - 1
        if (len >= 0) ans *= ways[len]
        prev = vip
    }
    val last = N - prev
    if (last >= 0) ans *= ways[last]

    println(ans)
}
