package `18week`

private val operators = listOf("+", "-", " ")
fun List<Int>.generateOperateCombinations(): List<String> {
    fun apply(index: Int, current: String): List<String> {
        if (index == size) return listOf(current)

        val combinations = mutableListOf<String>()
        for (operator in operators) {
            combinations.addAll(apply(index + 1, current + operator + this[index]))
        }
        return combinations
    }
    return apply(1, this[0].toString())
}

fun calculate(expression: String): Int {
    var result = 0
    var currentNumber = 0
    var lastOperator = '+'

    fun applyOperator(operator: Char, number: Int) {
        when (operator) {
            '+' -> result += number
            '-' -> result -= number
        }
    }

    for (char in expression) {
        if (char.isDigit()) {
            currentNumber = currentNumber * 10 + (char - '0')
        } else if (char == '+' || char == '-') {
            applyOperator(lastOperator, currentNumber)
            currentNumber = 0
            lastOperator = char
        }
    }
    applyOperator(lastOperator, currentNumber)
    return result
}

private val NL = "\n"

fun main() {
    val N = readln().toInt()
    println(Array(N) {
        val intList = (1..readln().toInt()).toList()
        val scripts = intList.generateOperateCombinations()
        scripts.mapNotNull { script ->
            val result: Boolean = calculate(script) == 0
            if (result) {
                script
            } else null
        }.sorted().joinToString(NL)
    }.joinToString("$NL$NL"))
}
