package `250712`

import java.util.PriorityQueue
import java.util.StringTokenizer

fun main() {
	val N = readln().toInt()
	val K = readln().toInt()
	val st = StringTokenizer(readln())
	val pq = PriorityQueue<Int>()
	repeat(N) {
		pq.add(st.nextToken().toInt())
	}
	val diffPQ = PriorityQueue<Int>()
	var prev = pq.poll()
	while (!pq.isEmpty()) {
		diffPQ.add(pq.peek() - prev)
		prev = pq.poll()
	}
	println(if (K >= N) 0 else Array(N - K) { diffPQ.poll() }.sum())
}
