package `12week`

fun main() {
    val N = readln().toInt()
    var budgetMax = 0
    var budgetSum = 0
    val budgets = readln().split(" ").map {
        val budget = it.toInt()
        budgetMax = Math.max(budgetMax, budget)
        budgetSum += budget
        budget
    }
    val budget = readln().toInt()
    var answer = Math.min(budget / N, budgetMax)
    var next = 0
    do {
        next = budgets.budgetSum(answer + 1)
        if (next <= budget && next <= budgetSum && answer < budgetMax) {
            answer++
        }
    } while (next < budget && next < budgetSum && answer < budgetMax)
    println(answer)
}

fun List<Int>.budgetSum(budget: Int) = this.fold(0) { acc, i ->
    acc + Math.min(budget, i)
}
