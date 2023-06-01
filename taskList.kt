import kotlin.system.exitProcess

class taskList {
    var taskNumber = 1
    var tasks: MutableMap<Int, String> = mutableMapOf()

    fun add(){
        println("Input a new task (enter a blank line to end):")
        var result = ""
        var input: String
        while (true) {
            input = readln().trim()
            if (input.all{it.isWhitespace()} && result.isEmpty()){
                println("The task is blank")
                break
            } else if (input.all{ it.isWhitespace() }) {
                break
            } else if (taskNumber < 10) {
                result += "  $input\n "
            } else if (taskNumber >= 10) {
                result += " $input\n "
            }
        }
        if(result.isNotEmpty()) {
            tasks[taskNumber] = result.trimEnd()
            taskNumber++
        }

    }

    fun printList(){
        if (tasks.isEmpty() || tasks[0] == ""){
            println("No tasks have been input")
        } else {
            for (i in tasks) {
                println()
                print("${i.key}${i.value}\n")
            }
            println()
        }
    }
}

fun main(){
    val listOfTasks = taskList()
    while(true) {
        println("Input an action (add, print, end):")
        val userInput = readln()
        when {
            userInput.equals("add", true) -> listOfTasks.add()
            userInput.equals("print", true) -> listOfTasks.printList()
            userInput.equals("end", true) -> {
                println("Tasklist exiting!")
                exitProcess(0)
            }
            else -> println("The input action is invalid")
        }
    }
}
