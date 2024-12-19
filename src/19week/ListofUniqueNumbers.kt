package `19week`

fun main() {
    readln()
    val arr = readln().split(" ").map { it.toInt() }
    val subArr = mutableSetOf<Int>()
    var start = 0
    var count = 0

    for (end in arr.indices) {
        while (subArr.contains(arr[end])) {
            subArr.remove(arr[start])
            start++
        }
        subArr.add(arr[end])
        count += end - start + 1
    }
    println(count)
}
