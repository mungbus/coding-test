package `250726`

class RepeatedDNASequences {
    fun findRepeatedDnaSequences(s: String): List<String> {
        val seen = mutableSetOf<String>()
        val repeated = mutableSetOf<String>()

        for (i in 0..s.length - 10) {
            val substring = s.substring(i, i + 10)
            if (!seen.add(substring)) {
                repeated.add(substring)
            }
        }

        return repeated.toList()
    }
}

fun main() {
    RepeatedDNASequences().apply {
        println(findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"))
        println(findRepeatedDnaSequences("AAAAAAAAAAAAA"))
    }
}
