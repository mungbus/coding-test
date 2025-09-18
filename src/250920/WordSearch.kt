package `250920`

class WordSearch {
    fun findWords(board: Array<CharArray>, words: Array<String>): List<String> {
        val result = mutableSetOf<String>()
        val rows = board.size
        val cols = board[0].size

        fun dfs(word: String, index: Int, x: Int, y: Int, visited: Array<BooleanArray>): Boolean {
            if (index == word.length) return true
            if (x !in 0..<rows || y !in 0..<cols || visited[x][y] || board[x][y] != word[index]) return false

            visited[x][y] = true
            val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            for ((dx, dy) in directions) {
                if (dfs(word, index + 1, x + dx, y + dy, visited)) {
                    visited[x][y] = false
                    return true
                }
            }
            visited[x][y] = false
            return false
        }

        for (word in words) {
            for (i in 0..<rows) {
                for (j in 0..<cols) {
                    val visited = Array(rows) { BooleanArray(cols) }
                    if (dfs(word, 0, i, j, visited)) {
                        result.add(word)
                        break
                    }
                }
            }
        }

        return result.toList()
    }
}

fun main() {
    val board = arrayOf(
        charArrayOf('o', 'a', 'a', 'n'),
        charArrayOf('e', 't', 'a', 'e'),
        charArrayOf('i', 'h', 'k', 'r'),
        charArrayOf('i', 'f', 'l', 'v')
    )
    val words = arrayOf("oath", "pea", "eat", "rain")
    val ws = WordSearch()
    println(ws.findWords(board, words))
}
