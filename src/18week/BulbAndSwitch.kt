package `18week`

fun toggle(arr: IntArray, idx: Int) {
    for (i in idx - 1..idx + 1) {
        if (i >= 0 && i < arr.size) {
            arr[i] = 1 - arr[i] // 0이면 1로, 1이면 0으로
        }
    }
}

fun minClicksToTarget(bulbs: IntArray, target: IntArray): Int {
    val n = bulbs.size
    var clicks = 0

    for (i in 0 until n) {
        if (bulbs[i] != target[i]) {
            if (i + 1 < n) {
                toggle(bulbs, i + 1)  // i번째가 다르면 그 다음 전구 클릭
                clicks++
            } else {
                return -1  // 마지막 전구인데 다른 경우 불가능
            }
        }
    }

    return if (bulbs.contentEquals(target)) clicks else -1
}

fun main() {
    readln()
    val before = readln().toCharArray().map { it.code }.toIntArray()
    val after = readln().toCharArray().map { it.code }.toIntArray()
    println(minClicksToTarget(before, after))
}
