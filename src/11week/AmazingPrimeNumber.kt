package `11week`

lateinit var nums: Array<Int>
var n = -1
val sb = StringBuilder()

fun main() {
    n = readln().toInt()
    nums = Array(n) { -1 }
    dfs(0)
    print(sb)
}

fun dfs(depth: Int) {
    if (depth == n) {
        if (isPrime(depth - 1)) {
            sb.append(nums.joinToString("")).append("\n")
        }
        return
    }
    val start = if (depth == 0) 2 else 0
    for (i in start until 10) {
        nums[depth] = i
        if (isPrime(depth)) dfs(depth + 1)
    }
}

fun isPrime(idx: Int): Boolean {
    var sum = 0
    for (i in 0..idx) {
        sum = sum * 10 + nums[i]
        for (j in 2 until sum) {
            if (sum % j == 0) return false
        }
    }
    return true
}
