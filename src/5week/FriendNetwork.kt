package `5week`

fun main() {
    val N = readln().toInt()
    repeat(N) {
        val friends = mutableSetOf<String>()
        val friendIndex = mutableMapOf<String, Int>()
        val friendMap = mutableMapOf<Int, Int>()
        val F = readln().toInt()
        repeat(F) {
            val result = linkedSetOf<Int>()
            val (a, b) = readln().split(" ")
            friends.add(a)
            val indexA = friends.size - 1
            friendIndex[a] = indexA
            friends.add(b)
            val indexB = friends.size - 1
            friendIndex[b] = indexB
            friendMap[indexA] = indexB
            friendMap[indexB] = indexA
            setOf(indexA, indexB).forEach {
                var temp: Int? = it
                do {
                    temp = friendMap[temp]
                    temp?.let {
                        if (result.contains(it)) {
                            return@forEach
                        } else {
                            result.add(it)
                        }
                    }
                } while (friendMap[temp] != null)
            }
            println(result.size)
        }
    }
}
