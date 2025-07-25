package `250601`

fun isAcceptable(password: String): Boolean {
	val vowels = setOf('a', 'e', 'i', 'o', 'u')
	var hasVowel = false
	var vowelCnt = 0
	var consonantCnt = 0
	var prevChar: Char? = null

	for (c in password) {
		if (c in vowels) {
			hasVowel = true
			vowelCnt++
			consonantCnt = 0
		} else {
			consonantCnt++
			vowelCnt = 0
		}

		if (vowelCnt == 3 || consonantCnt == 3) return false

		if (prevChar != null && c == prevChar) {
			if (!(c == 'e' || c == 'o')) return false
		}
		prevChar = c
	}
	return hasVowel
}

fun main() {
	var str: String
	while (true) {
		str = readln()
		if (str == "end") {
			break
		}
		if (isAcceptable(str)) {
			println("<$str> is acceptable.")
		} else {
			println("<$str> is not acceptable.")
		}
	}
}
