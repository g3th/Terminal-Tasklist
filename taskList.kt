import kotlin.system.exitProcess
import kotlinx.datetime.*

class TaskList {
    private lateinit var givenTaskDate: LocalDate
    private var taskIsBeingEdited = false
    private lateinit var tag: String
    private lateinit var currentTimeDateAndTaskPriority: String
    private lateinit var inputtedDate: List<String>
    private lateinit var givenTaskDateAndTime: String
    private var result: String = ""
    private var taskPriority: String = ""
    private val taskPriorities = listOf("C", "H", "N", "L")
    var tasks = mutableListOf<String>()
    private val currentTime = Clock.System.now()
        .toLocalDateTime(TimeZone.of("UTC+2"))
        .date /* Time zone is probably wrong.
              */
    fun addTask() {
        priority()
        date()
        time()
        task()
    }

    fun deleteTask(){
        printTasks()
        while (true) {
            println("Input the task number (1-${tasks.size}):")
            try {
                val taskIndex = readln().toInt()
                when {
                    taskIndex < 1 || taskIndex > tasks.size -> println("Invalid task number")
                    else -> {
                        tasks.removeAt(taskIndex - 1)
                        println("The task is deleted")
                        break
                    }
                }
            }catch(e: NumberFormatException){
                println("Invalid task number")
            }
        }
    }

    fun editTask() {
        val currentTaskPriority = taskPriority
        val currentTaskDate = givenTaskDate
        printTasks()
        topOfLoop@ while (true) {
            println("Input the task number (1-${tasks.size}):")
            try {
                val taskIndex = readln().toInt()
                when {
                    taskIndex < 1 || taskIndex > tasks.size -> println("Invalid task number")
                    else -> {
                        while (true) {
                            println("Input a field to edit (priority, date, time, task):")
                            val input = readln()
                            when {
                                input.equals("priority", true) -> {
                                    val newPriority = priority()
                                    val assignNewPriority = tasks[taskIndex - 1]
                                        .replaceFirst(" $currentTaskPriority ", " $newPriority ")
                                    tasks[taskIndex - 1] = assignNewPriority
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("date", true) -> {
                                    val currentTag = tag
                                    date()
                                    val assignNewDate = tasks[taskIndex - 1]
                                        .replaceFirst(currentTaskDate.toString(), givenTaskDate.toString())
                                        .replaceFirst(currentTag, tag)
                                    tasks[taskIndex - 1] = assignNewDate
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("time", true) -> {
                                    val currentTime = givenTaskDateAndTime.split("T")[1]
                                    time()
                                    val assignNewTime = tasks[taskIndex - 1]
                                        .replaceFirst(currentTime, givenTaskDateAndTime.split("T")[1])
                                    tasks[taskIndex - 1] = assignNewTime
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("task", true) -> {
                                    taskIsBeingEdited = true
                                    val currentTask = tasks[taskIndex - 1].split("\n").drop(1)
                                    val newTask = task()
                                    val assignNewTask = tasks[taskIndex - 1]
                                        .replace(currentTask.joinToString("\n"), newTask)
                                    tasks[taskIndex - 1] = assignNewTask
                                    println("The task is changed")
                                    taskIsBeingEdited = false
                                    break@topOfLoop
                                }
                                else -> println("Invalid field")
                            }
                        }
                    }
                }
            }catch(e: NumberFormatException){
                println("Invalid task number")
            }
        }
    }

    private fun task(): String {
        result=""
        var input: String
        println("Input a new task (enter a blank line to end):")
        while (true) {
            input = readln().trim()
            if (input.all { it.isWhitespace() } && result.isEmpty()) {
                println("The task is blank")
                break
            } else if (input.all { it.isWhitespace() }) {
                break
            } else if (tasks.size < 10) {
                result += "   $input\n"
            } else {
                result += "   $input\n"
            }
        }
        if (result.isNotEmpty() && !taskIsBeingEdited) {
            if (tasks.size < 9) {
                tasks.add("  ${currentTimeDateAndTaskPriority}\n${result.trimEnd()}")
            } else {
                tasks.add(" ${currentTimeDateAndTaskPriority}\n${result.trimEnd()}")
            }
        }
        return result
    }
    private fun date(){
        while(true) {
            println("Input the date (yyyy-mm-dd):")
            inputtedDate = readln().split("-")
            try {
                givenTaskDate = LocalDate(inputtedDate[0].toInt(), inputtedDate[1].toInt(), inputtedDate[2].toInt())
                //Due Tag is invoked and assigned here ->
                tag = appendDueTag(givenTaskDate)
                break
            } catch (e: Exception) {
                println("The input date is invalid")
            }
        }
    }

    private fun time(){
        while(true) {
            println("Input the time (hh:mm):")
            val inputtedTime = readln().split(":")
            try {
                givenTaskDateAndTime = LocalDateTime(inputtedDate[0].toInt(), inputtedDate[1].toInt(),
                    inputtedDate[2].toInt(),inputtedTime[0].toInt(),inputtedTime[1].toInt()).toString()

                /* Rewrite to include LocalTime
                   (update datetime to 0.4.0 instead of 0.3.1)
                */

                currentTimeDateAndTaskPriority = "${givenTaskDateAndTime
                    .replace("T"," ")} $taskPriority $tag"
                break
            } catch (e:Exception) {
                println("The input time is invalid")
            }
        }
    }

    private fun appendDueTag(date: LocalDate): String{
        lateinit var dueTag: String
        when {
            date.daysUntil(currentTime) > 0 -> dueTag = "O"
            date.daysUntil(currentTime) < 0 -> dueTag = "I"
            date.daysUntil(currentTime) == 0 -> dueTag = "T"
        }
        return dueTag
    }

    private fun priority(): String{
        while (true){
            println("Input the task priority (C, H, N, L):")
            val input = readln()
            when {
                taskPriorities.any{ it.equals(input,true) } -> {
                    taskPriority = input
                    break
                }
            }
        }
        return taskPriority
    }

    fun printTasks(){
        var counter = 1
        for (i in tasks) {
            println()
            print("$counter${i}\n")
            counter++
        }
        println()
    }
}

fun main() {
    val actionList = listOf("print", "edit", "delete")
    val listOfTasks = TaskList()
    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        val userInput = readln()
        when {
            actionList.any { userInput.equals(it, true) } && listOfTasks.tasks.isEmpty() -> {
                println("No tasks have been input")
            }
            userInput.equals("add", true) -> listOfTasks.addTask()
            userInput.equals("print", true) -> listOfTasks.printTasks()
            userInput.equals("delete", true) -> listOfTasks.deleteTask()
            userInput.equals("edit", true) -> listOfTasks.editTask()
            userInput.equals("end", true) -> {
                println("Tasklist exiting!")
                exitProcess(0)
            }
            else -> println("The input action is invalid")
        }
    }
}
