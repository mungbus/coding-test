package `250712`

fun getHint(secret: String, guess: String): String {
	var bulls = 0
	var cows = 0
	val count = IntArray(10)
	for (i in secret.indices) {
	    val s = secret[i].digitToInt()
	    val g = guess[i].digitToInt()
	    if (s == g) {
			bulls++
		} else {
	        if (count[s] < 0) cows++
	        if (count[g] > 0) cows++
	        count[s]++
	        count[g]--
		}
	}
	return "${bulls}A${cows}B"
}

fun main() {
	println(getHint("1807", "7810")) // Output: "1A3B"
}
