package `250525`

import kotlin.math.abs

fun main() {
	readln()
	val solList = readln().split(" ").map { it.toInt() }
	var min = Int.MAX_VALUE
	var pair = Pair(-1, -1)
	for (i in 0 until solList.size) {
		for (j in i + 1 until solList.size) {
			if (i == j) continue
			val v = abs(solList[i] + solList[j])
			if (v <= min) {
				pair = Pair(i, j)
				min = v
			}
		}
	}
	println(listOf(solList[pair.first], solList[pair.second]).sorted().joinToString(" "))
}
