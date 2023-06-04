import kotlin.system.exitProcess
import kotlinx.datetime.*

class TaskList {
    private lateinit var currentTimeDateAndTaskPriority: String
    private var taskPriority: String = ""
    private val taskPriorities = listOf("C", "H", "N", "L")
    private var taskNumber = 1
    private var tasks: MutableMap<Int, String> = mutableMapOf()

    fun add(){
        priority()
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
                result += "   $input\n"
            } else if (taskNumber >= 10) {
                result += "   $input\n"
            }
        }
        if(result.isNotEmpty()) {
            if (taskNumber < 10) {
                tasks[taskNumber] = "  ${currentTimeDateAndTaskPriority}\n${result.trimEnd()}"
            } else {
                tasks[taskNumber] = " ${currentTimeDateAndTaskPriority}\n${result.trimEnd()}"
            }
            taskNumber++
        }
        taskPriority = ""
    }

    private fun timeDate(){
        topOfLoop@ while(true) {
            println("Input the date (yyyy-mm-dd):")
            val date = readln().split("-")
            try {
                LocalDate(date[0].toInt(), date[1].toInt(), date[2].toInt())
                while(true) {
                    println("Input the time (hh:mm):")
                    val time = readln().split(":")
                    try {
                        LocalDateTime(date[0].toInt(), date[1].toInt(),
                            date[2].toInt(),time[0].toInt(),time[1].toInt())
                        //LocalTime(time[0].toInt(),time[1].toInt())
                        currentTimeDateAndTaskPriority = "${LocalDateTime(date[0].toInt(), date[1].toInt(),
                                                         date[2].toInt(),time[0].toInt(),time[1].toInt())
                                                        .toString().replace("T"," ")} $taskPriority"
                        break@topOfLoop
                    } catch (e:Exception) {
                        println("The input time is invalid")
                    }
                }
            } catch (e: Exception) {
                println("The input date is invalid")
            }
        }
    }

    private fun priority(){
        while (true){
            println("Input the task priority (C, H, N, L):")
            val input = readln()
            when {
                taskPriorities.any{ it.equals(input,true) } -> {
                    taskPriority += input
                    timeDate()
                    break
                }
            }
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
    val listOfTasks = TaskList()
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
