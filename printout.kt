package tasklist

data class tasks (val tasksTDP: List<String>, val task: List<String>)

class PrintOut {

    val header_top = """+----+------------+-------+---+---+--------------------------------------------+
| N  |    Date    | Time  | P | D |                   Task                     |
+----+------------+-------+---+---+--------------------------------------------+
"""
    val header_bottom = "\n+----+------------+-------+---+---+--------------------------------------------+"

    fun taskLayout(input: String): String {
        var printoutInput = ""
        val strList = input.split("\n")
        var chunkedList = mutableListOf<String>()
        for (i in strList) {
            chunkedList += i.chunked(44)
        }
        for(i in chunkedList.indices){
            if (chunkedList[i].length < 44) {
                if (i == 0){
                    printoutInput += "${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}|"

                } else {
                    printoutInput += "\n|    |            |       |   |   |${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}|"
                }
            } else {
                if (i == 0){
                    printoutInput += "${chunkedList[i]}|"
                } else {
                    printoutInput += "\n|    |            |       |   |   |${chunkedList[i]}|"
                }
            }
        }
        return printoutInput
    }

    fun printTasks(tasksDateTimePriority: MutableList<String>, tasks:MutableList<String>) {
        val tasklist = mutableListOf(tasks(tasksDateTimePriority, tasks))
        var counter = 1
        print(header_top)
        for ((a,b) in tasklist) {
            for (c in a.indices) {
                print("| $counter  | ${a[c]} |${b[c]}")
                println(header_bottom)
                counter++
            }
        }
        println()
    }
}
