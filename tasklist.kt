package tasklist

import kotlin.system.exitProcess
import kotlinx.datetime.*
import java.io.File

fun clearScreen(){
	ProcessBuilder("clear").redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor()
}

class TaskList {
    val storeJson = mutableListOf<TasksToJson?>()
    private val file = File("save/tasklist.json")
    lateinit var givenTaskDate: LocalDate
    private var taskIsBeingEdited = false
    lateinit var tag: String
    private lateinit var currentTimeDateAndTaskPriority: String
    private lateinit var inputtedDate: List<String>
    lateinit var givenTaskDateAndTime: String
    var result: String = ""
    var taskPriority: String = ""
    private val taskPriorities = listOf("C", "H", "N", "L")
    var tasksTimeDatePriority = mutableListOf<String>()
    var tasks = mutableListOf<String>()
    private val currentTime = Clock.System.now()
        .toLocalDateTime(TimeZone.of("UTC+1"))
        .date
    var jsonTaskList = ""

    init {
        if (file.exists()) {
            SaveReadJsonFile().readIt(storeJson)
            for (i in SaveReadJsonFile().readJsonAndFillTasks(tasksTimeDatePriority, storeJson).component2()) {
                if (i == ""){
                    tasks.clear()
                } else {
                    tasks.add(PrintOut().taskLayout(i))
                }
            }
            file.delete()
        }
    }

    fun addTask() {
        priority()
        date()
        time()
        task()
    }

    fun deleteTask() {
        while (true) {
            println("Input the task number (1-${tasksTimeDatePriority.size}):")
            try {
                val taskIndex = readln().toInt()
                when {
                    taskIndex < 1 || taskIndex > tasksTimeDatePriority.size -> println("Invalid task number")
                    else -> {
                        tasksTimeDatePriority.removeAt(taskIndex - 1)
                        tasks.removeAt(taskIndex -1)
                        println("The task is deleted")
                        break
                    }
                }
            } catch (e: NumberFormatException) {
                println("Invalid task number")
            }
        }
    }

    fun editTask() {
        val currentTaskPriority = taskPriority
        val currentTaskDate = givenTaskDate
        topOfLoop@ while (true) {
            println("Input the task number (1-${tasksTimeDatePriority.size}):")
            try {
                val taskIndex = readln().toInt()
                when {
                    taskIndex < 1 || taskIndex > tasksTimeDatePriority.size -> println("Invalid task number")
                    else -> {
                        while (true) {
                            println("Input a field to edit (priority, date, time, task):")
                            val input = readln()
                            when {
                                input.equals("priority", true) -> {
                                    val newPriority = priority()
                                    val assignNewPriority = tasksTimeDatePriority[taskIndex - 1]
                                        .replaceFirst(" $currentTaskPriority ", " $newPriority ")
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewPriority
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("date", true) -> {
                                    val currentTag = tag
                                    date()
                                    val assignNewDate = tasksTimeDatePriority[taskIndex - 1]
                                        .replaceFirst(currentTaskDate.toString(), givenTaskDate.toString())
                                        .replaceFirst(currentTag, tag)
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewDate
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("time", true) -> {
                                    val currentTime = givenTaskDateAndTime.split("T")[1]
                                    time()
                                    val assignNewTime = tasksTimeDatePriority[taskIndex - 1]
                                        .replaceFirst(currentTime, givenTaskDateAndTime.split("T")[1])
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewTime
                                    println("The task is changed")
                                    break@topOfLoop
                                }

                                input.equals("task", true) -> {
                                    taskIsBeingEdited = true
                                    val newTask = task()
                                    tasks[taskIndex - 1] = newTask
                                    println("The task is changed")
                                    taskIsBeingEdited = false
                                    break@topOfLoop
                                }
                            }
                        }
                    }
                }
            } catch (e: NumberFormatException) {
                println("Invalid task number")
            }
        }
    }

    private fun task(): String {
        var counter = 0
        var tempAppend = ""
        var input: String
        println("Input a new task (enter a blank line to end):")
        while (true) {
            input = readln()
            if (input.all { it.isWhitespace() } && tempAppend.isEmpty()) {
                println("The task is blank")
                break
            } else if (input.all { it.isWhitespace() }) {
                break
            } else {
                if (counter == 0) {
                    tempAppend += input
                } else {
                    tempAppend += "\n$input"
                }
            }
            counter++
        }
        jsonTaskList += tempAppend
        result = PrintOut().taskLayout(tempAppend)
        if (result.isNotEmpty() && !taskIsBeingEdited) {
            tasksTimeDatePriority.add(
                "${givenTaskDate} | ${givenTaskDateAndTime.split("T")[1]} | ${taskPriority} | $tag"
            )
            tasks.add(result)
        }
        return result
    }

    private fun date() {
        while (true) {
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

    private fun time() {
        while (true) {
            println("Input the time (hh:mm):")
            val inputtedTime = readln().split(":")
            try {
                givenTaskDateAndTime = LocalDateTime(
                    inputtedDate[0].toInt(), inputtedDate[1].toInt(),
                    inputtedDate[2].toInt(), inputtedTime[0].toInt(), inputtedTime[1].toInt()
                ).toString()

                /* This works with datetime 0.4.0:
                   'LocalTime(time[0].toInt(),time[1].toInt())'
                   But Hyperskill have the outdated 0.3.1
                  */

                currentTimeDateAndTaskPriority = "${
                    givenTaskDateAndTime
                        .replace("T", " ")
                } $taskPriority $tag"
                break
            } catch (e: Exception) {
                println("The input time is invalid")
            }
        }
    }

    private fun appendDueTag(date: LocalDate): String {
        lateinit var dueTag: String
        when {
            date.daysUntil(currentTime) > 0 -> dueTag = "\u001B[101m \u001B[0m" //O
            date.daysUntil(currentTime) < 0 -> dueTag = "\u001B[102m \u001B[0m" //I
            date.daysUntil(currentTime) == 0 -> dueTag = "\u001B[103m \u001B[0m" //T
        }
        return dueTag
    }

    private fun priority(): String {
        while (true) {
            println("Input the task priority (C, H, N, L):")
            val input = readln()
            when {
                taskPriorities.any { it.equals(input, true) } -> {
                    when {
                        input.equals("C", ignoreCase = true) -> taskPriority = "\u001B[101m \u001B[0m"
                        input.equals("H", ignoreCase = true) -> taskPriority = "\u001B[103m \u001B[0m"
                        input.equals("N", ignoreCase = true) -> taskPriority = "\u001B[102m \u001B[0m"
                        input.equals("L", ignoreCase = true) -> taskPriority = "\u001B[104m \u001B[0m"
                    }
                    break
                }
            }
        }
        result = ""
        return taskPriority
    }
}

fun main() {
	clearScreen()
    val tasks = TaskList()
    val print = PrintOut()
    val actionList = listOf("print", "edit", "delete")
    val savedList = SaveReadJsonFile()
    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        val userInput = readln()
        when {
            actionList.any { userInput.equals(it, true) } && tasks.tasks.isEmpty() -> {
                println("No tasks have been input")
            }
            userInput.equals("add", true) -> {
                tasks.addTask()
                tasks.storeJson.add(savedList.formatJson(tasks.givenTaskDate.toString(),
                    tasks.givenTaskDateAndTime,
                    tasks.taskPriority,
                    tasks.tag,
                    tasks.jsonTaskList))
                tasks.jsonTaskList = ""
            }
            userInput.equals("print", true) -> {
            	clearScreen()
                print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
            }
            userInput.equals("delete", true) -> {
            	clearScreen()
                print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
                tasks.deleteTask()
            }
            userInput.equals("edit", true) -> {
            	clearScreen()
                print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
                tasks.editTask()
            }
            userInput.equals("end", true) -> {
                println("Tasklist exiting!")
                savedList.saveJson(tasks.storeJson)
                exitProcess(0)
            }
            else -> println("The input action is invalid")
        }
    }
}
