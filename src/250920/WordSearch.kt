package `250920`

class WordSearch {
    fun findWords(board: Array<CharArray>, words: Array<String>): List<String> {
        val result = mutableSetOf<String>()
        val trie = Trie()

        // Build the Trie by words
        for (word in words) {
            trie.insert(word)
        }

        val rows = board.size
        val cols = board[0].size
        val visited = Array(rows) { BooleanArray(cols) }

        fun dfs(x: Int, y: Int, node: TrieNode, path: StringBuilder) {
            if (x !in 0..<rows || y !in 0..<cols || visited[x][y]) return

            val char = board[x][y]
            val childNode = node.children[char] ?: return // No matching child, backtrack

            path.append(char)
            if (childNode.isWord) {
                result.add(path.toString())
            }

            visited[x][y] = true
            val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            for ((dx, dy) in directions) {
                dfs(x + dx, y + dy, childNode, path)
            }
            visited[x][y] = false
            path.deleteCharAt(path.lastIndex)
        }

        for (i in 0..<rows) {
            for (j in 0..<cols) {
                dfs(i, j, trie.root, StringBuilder())
            }
        }

        return result.toList()
    }

    class Trie {
        val root = TrieNode()

        fun insert(word: String) {
            var current = root
            for (char in word) {
                current = current.children.computeIfAbsent(char) { TrieNode() }
            }
            current.isWord = true
        }
    }

    class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isWord = false
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
