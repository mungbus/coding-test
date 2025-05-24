package `250525`

fun spaceCalc(spaces: List<Int>): Int {
	val temp = mutableListOf<Int>()
	for (i in 0 until spaces.size) {
		for (j in i until spaces.size) {
			val append = if (spaces[i] > spaces[j]) 2
			else if (spaces[i] < spaces[j]) 1
			else 0
			temp.add(append)
		}
	}
	return temp.joinToString("").toInt(3)
}

fun pairCount(n: Int): Int {
	return n * (n - 1) / 2
}

fun main() {
	val (M) = readln().split(" ").map { it.toInt() }
	val spaces = List(M) {
		val space = readln().split(" ").map { it.toInt() }
		spaceCalc(space)
	}
	val spaceCount = spaces.groupingBy { it }.eachCount()
	val result = spaceCount.values.filter { it > 1 }.sumOf { pairCount(it) }
	println(result)
}