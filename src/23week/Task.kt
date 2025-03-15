package `23week`

import java.util.LinkedList

data class Task(
	val index: Int,
	val time: Int,
	val required: Set<Int>,
	val isSuccess: Boolean = false
) {

}

fun main() {
	// 7
	//5 0
	//1 1 1
	//3 1 2
	//6 1 1
	//1 2 2 4
	//8 2 2 4
	//4 3 3 5 6
	val N = readln().toInt()
	val successTasks = mutableSetOf<Int>()
	val tasks = Array(N) { index ->
		val list = readln().split(" ").map { it.toInt() }
		val time = list.first()
		val required = list.drop(2).toSet()
		Task(index, time, required)
	}.toMutableList()
}