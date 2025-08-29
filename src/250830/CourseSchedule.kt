package `250830`

class CourseSchedule {
    fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        val graph = Array(numCourses) { mutableListOf<Int>() }
        val require = IntArray(numCourses)

        for ((course, prereq) in prerequisites) {
            graph[prereq].add(course)
            require[course]++
        }

        val finished = ArrayDeque<Int>()
        repeat(numCourses) {
            if (require[it] == 0) {
                finished.add(it)
            }
        }

        var completedCourses = 0
        while (finished.isNotEmpty()) {
            val course = finished.removeFirst()
            completedCourses++

            graph[course].forEach { nextCourse ->
                require[nextCourse]--
                if (require[nextCourse] == 0) {
                    finished.add(nextCourse)
                }
            }
        }

        return completedCourses == numCourses
    }
}

fun main() {
    val cs = CourseSchedule()
    println(cs.canFinish(2, arrayOf(intArrayOf(1, 0))) == true)
    println(cs.canFinish(2, arrayOf(intArrayOf(1, 0), intArrayOf(0, 1))) == false)
}
