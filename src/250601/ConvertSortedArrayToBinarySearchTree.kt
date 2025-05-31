package `250601`

class TreeNode(
	val `val`: Int,
	var left: TreeNode? = null,
	var right: TreeNode? = null
)

fun sortedArrayToBST(nums: IntArray): TreeNode? {
    fun helper(left: Int, right: Int): TreeNode? {
        if (left > right) return null
        // 오른쪽 중간값 우선 선택
        val mid = (left + right + 1) / 2
        val node = TreeNode(nums[mid])
        node.left = helper(left, mid - 1)
        node.right = helper(mid + 1, right)
        return node
    }
    return helper(0, nums.size - 1)
}

fun treeToArray(root: TreeNode?): List<Int?> {
	val result = mutableListOf<Int?>()
	val queue = ArrayDeque<TreeNode?>()
	queue.add(root)
	while (queue.isNotEmpty()) {
		val node = queue.removeFirst()
		if (node != null) {
			result.add(node.`val`)
			queue.add(node.left)
			queue.add(node.right)
		} else {
			result.add(null)
		}
	}
	while (result.isNotEmpty() && result.last() == null) {
		result.removeAt(result.size - 1)
	}
	return result
}

fun main() {
	val nums = intArrayOf(-10, -3, 0, 5, 9)
	val root = sortedArrayToBST(nums)
	val result = treeToArray(root)
	println(result) // [0, -3, 9, -10, null, 5]
}
