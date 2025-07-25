package `250712`

fun main() {
	readln()
	val switch = readln().split(" ").map { it == "1" }.toMutableList()
	val N = readln().toInt()
	repeat(N) {
		val (a, b) = readln().split(" ").map { it.toInt() }
		if (a == 1) {
			for (i in b - 1 until switch.size step b) {
				switch[i] = !switch[i]
			}
		} else {
			val index = b - 1
			var left = index
			var right = index
			while (left > 0 && right < switch.size - 1 && switch[left - 1] == switch[right + 1]) {
				left--
				right++
			}
			for (i in left..right) {
				switch[i] = !switch[i]
			}
		}
	}

	switch.map { if (it) 1 else 0 }
	    .chunked(20)
	    .forEach { println(it.joinToString(" ")) }
}
