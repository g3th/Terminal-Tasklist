package tasklist

import kotlin.system.exitProcess
import kotlinx.datetime.*
import java.io.File

fun clearScreen(){
	ProcessBuilder("clear").redirectOutput(ProcessBuilder.Redirect.INHERIT).start().waitFor()
}

class TaskList {

	private lateinit var currentTimeDateAndTaskPriority: String
    private lateinit var inputtedDate: List<String>   
    private val file = File("save/tasklist.json")
    private val taskPriorities = listOf("1", "2", "3", "4")
    private var taskIsBeingEdited = false
    private val currentTime = Clock.System.now()
    .toLocalDateTime(TimeZone.of("UTC+1"))
    .date
    lateinit var givenTaskDate: LocalDate
    lateinit var tag: String
    lateinit var givenTaskDateAndTime: String
    var storeJson = mutableListOf<TasksToJson?>()
    var result: String = ""
    var taskPriority: String = ""
    var tasksTimeDatePriority = mutableListOf<String>()
    var tasks = mutableListOf<String>()
    var jsonTaskList = ""
	/* 	Check for previously saved Tasklist
		If it exists, load it and populate the lists.
	*/
	data class ReformatColours(val date: String, val time: String, val priority: String, val due: String)
    init {
        if (file.exists()){
        	SaveReadJsonFile().readIt(storeJson)
        	val filledTasks = SaveReadJsonFile().readJsonAndFillTasks(tasksTimeDatePriority, storeJson)
			tasksTimeDatePriority = filledTasks.timeDate
			for (j in tasksTimeDatePriority.indices){	
				val tempStore = ReformatColours(tasksTimeDatePriority[j].split("|")[0], tasksTimeDatePriority[j].split("|")[1], tasksTimeDatePriority[j].split("|")[2], tasksTimeDatePriority[j].split("|")[3])
				tasksTimeDatePriority[j] = ColourEditor().reformatColours(tasksTimeDatePriority[j], tempStore)
			}
			for (i in filledTasks.listOfTasks){
				tasks.add(PrintOut().taskLayout(i))
				}
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
                        tasks.removeAt(taskIndex - 1)
                        storeJson.removeAt(taskIndex -1)
                        println("The task is deleted")
                        break
                    }
                }
            } catch (e: NumberFormatException) {
                println("Invalid task number")
            }
        }
    }
	/* 	Edit Mode:
		For every field, change both lists, the one to be saved to file (json)
		and the ones used by the program.
	*/
        fun editTask() {
        topOfLoop@ while (true) {
            println("Input the task number (1-${tasksTimeDatePriority.size}):")
            try {
                val taskIndex = readln().toInt()
				val currentTaskPriority = storeJson[taskIndex - 1]!!.priority
				val currentTaskDate = storeJson[taskIndex - 1]!!.date
				inputtedDate = listOf(tasksTimeDatePriority[0].split("-")[0],
				tasksTimeDatePriority[0].split("-")[1],
				tasksTimeDatePriority[0].split("-")[2].split(" ")[0])
				tag = tasksTimeDatePriority[0].split("|")[3].replace(" ","")
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
                                        .replace("$currentTaskPriority", "$newPriority")
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewPriority
                                    storeJson[taskIndex -1]!!.priority = newPriority
                                }
                                input.equals("date", true) -> {
                                    val currentTag = tag
                                    date()
                                    val assignNewDate = tasksTimeDatePriority[taskIndex - 1]
                                        .replaceFirst(currentTaskDate.toString(), givenTaskDate.toString())
                                        .replaceFirst(currentTag, tag)
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewDate
                                    storeJson[taskIndex -1]!!.date = givenTaskDate.toString()
                                }
                                input.equals("time", true) -> {
                                    val currentTime = tasksTimeDatePriority[taskIndex - 1].split(" | ")[1]
                                    time()
                                    val assignNewTime = tasksTimeDatePriority[taskIndex - 1]
                                        .replaceFirst(currentTime, givenTaskDateAndTime.split("T")[1])
                                    tasksTimeDatePriority[taskIndex - 1] = assignNewTime
                                }
                                input.equals("task", true) -> {
                                    taskIsBeingEdited = true
                                    val newTask = task()
                                    tasks[taskIndex - 1] = newTask
                                    taskIsBeingEdited = false                                    
                                }
                            }                           
							println("\nThe task is changed")
							println("\nPress Enter to Continue...")
							readln()
							break@topOfLoop
                        }
                    }
                }
            } catch (e: NumberFormatException) {
                println("Invalid task number")
				println("\nPress Enter to Continue...")
				readln()
            }
        }
    }
	/*  Task Creation:
		This is an entirely separate list from
		time, date and priority, and contains
		only tasks. This is so chunking, layout and
		printing is easier.
	*/
    private fun task(): String {
        var counter = 0
        var tempAppend = ""
        var input: String
        clearScreen()
        PrintOut().table()
        println("Input a new task:")
        println("-----------------")
        println("1. Enter a single/multi-line task by hitting enter after your input.")
        println("2. Finish by hitting enter without inputting anything.\n")
        while (true) {
	       	print("> ")
            input = readln()
            if (input.all { it.isWhitespace() } && tempAppend.isEmpty()) {
                println("\nThe task is blank")
                println("Press Enter to Continue...")
                readln()
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
                "${ColourEditor().escSeq}${ColourEditor().setText}m${givenTaskDate} ${ColourEditor().escSeq}${ColourEditor().setBorder}m| ${ColourEditor().escSeq}${ColourEditor().setText}m${givenTaskDateAndTime.split("T")[1]} ${ColourEditor().escSeq}${ColourEditor().setBorder}m| ${taskPriority} | ${ColourEditor().escSeq}${ColourEditor().setText}m$tag"
            )
            tasks.add(result)
        }
        return result
    }
	/*  Below are Time, Date, Priority and Tag assignment.
		This is grouped all in a single list.
		This way print layout is easier.
	*/
    private fun date() {
        while (true) {
        	clearScreen()
        	PrintOut().table()
            println("Input the date (yyyy-mm-dd):")
            println("----------------------------") 
            print("> ")
            inputtedDate = readln().split("-")
            try {
                givenTaskDate = LocalDate(inputtedDate[0].toInt(), inputtedDate[1].toInt(), inputtedDate[2].toInt())
                //Due Tag is invoked and assigned here ->
                tag = appendDueTag(givenTaskDate)
                break
            } catch (e1: java.lang.IllegalArgumentException) {
					println("\nInvalid Date.")
					println("Press Enter To Continue...")
					readln()
    		} catch (e2: java.lang.IndexOutOfBoundsException) {
        			println("\nIncomplete Date.")
        			println("Press Enter To Continue...")
        			readln()
				}
    		}
    	}

    private fun time() {

        while (true) {
        	clearScreen()
        	PrintOut().table()
            println("Input the time (hh:mm):")
            println("-----------------------") 
            print("> ")
            val inputtedTime = readln().split(":")
            try {
                givenTaskDateAndTime = LocalDateTime(
                    inputtedDate[0].toInt(), inputtedDate[1].toInt(),
                    inputtedDate[2].toInt(), inputtedTime[0].toInt(), inputtedTime[1].toInt()
                ).toString()
                currentTimeDateAndTaskPriority = "${
                    givenTaskDateAndTime
                        .replace("T", " ")
                } $taskPriority $tag"
                break
            } catch (e1: java.lang.IndexOutOfBoundsException) {
                println("Incomplete Time")
                println("Press Enter to Continue...")
                readln()
            } catch (e2: Exception){
            	println("Invalid Time")
            	println("Press Enter to Continue...")
            	readln()
            }
        }
    }

    private fun appendDueTag(date: LocalDate): String {
        lateinit var dueTag: String
        when {
            date.daysUntil(currentTime) > 0 -> dueTag = "\u001B[38;5;196m█${ColourEditor().escSeq}${ColourEditor().setBorder}m" //O 
            date.daysUntil(currentTime) < 0 -> dueTag = "\u001B[38;5;76m█${ColourEditor().escSeq}${ColourEditor().setBorder}m" //I
            date.daysUntil(currentTime) == 0 -> dueTag = "\u001B[38;5;226m█${ColourEditor().escSeq}${ColourEditor().setBorder}m" //T
        }
        return dueTag
    }

    private fun priority(): String {
        while (true) {
        	clearScreen()
        	PrintOut().table()
            println("Input a task priority:\n\n1. Critical\n2. High\n3. Normal\n4. Low")
            print("> ")
            val input = readln()
            when {
                taskPriorities.any { it == input } -> {
                    when (input) {
                       "1"-> taskPriority = "\u001B[38;5;196m█${ColourEditor().escSeq}${ColourEditor().setBorder}m"// Red
                       "2"-> taskPriority = "\u001B[38;5;226m█${ColourEditor().escSeq}${ColourEditor().setBorder}m"// Yellow
                       "3"-> taskPriority = "\u001B[38;5;76m█${ColourEditor().escSeq}${ColourEditor().setBorder}m"// Green
                       "4"-> taskPriority = "\u001B[38;5;45m█${ColourEditor().escSeq}${ColourEditor().setBorder}m"// Blue
                    }
                    break
                }
                else -> {
                	println("Invalid Input")
					println("\nPress Enter to Continue...")
					readln()
					}
            }
        }
        result = ""
        return taskPriority
    }
}

fun main() {
	val colours = ColourEditor()
	val print = PrintOut()
    val tasks = TaskList()
    val actionList = listOf("2", "4", "5") //These are actions which won't function with an empty task list.
    val savedList = SaveReadJsonFile()
    val date = Clock.System.now()
    .toLocalDateTime(TimeZone.of("UTC+1")).date
    val time = "${Clock.System.now().toLocalDateTime(TimeZone.of("UTC+1")).toString().split("T")[1].split(":")[0]}:${Clock.System.now().toLocalDateTime(TimeZone.of("UTC+1")).toString().split("T")[1].split(":")[1]}"
    while (true) {
    	clearScreen()
    	print.table()
    	println("Date: ${date}\nTime: ${time}\n")
    	print.userOptions()
        print("\n> ")
        val userInput = readln()
        when {
	        actionList.any { userInput.equals(it, true) } && tasks.tasks.isEmpty() -> {
	            println("\nNo tasks have been input")
                println("Press Enter To Continue...")
                readln()
	        }
            // Add Task
            userInput == "1" -> {
                tasks.addTask()
                tasks.storeJson.add(savedList.formatJson(tasks.givenTaskDate.toString(),
                    tasks.givenTaskDateAndTime,
                    tasks.taskPriority,
                    tasks.tag,
                   tasks.jsonTaskList))
                tasks.jsonTaskList = ""
            }
            // Print Task
            userInput == "2" -> {
            	clearScreen()
            	print.table()
				print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
				println("\nPress Enter To Continue...")
				readln()
            }
            // Set Layout Colours
            userInput == "3" -> {
            	clearScreen()
				print.table()
				val oldText = "${ColourEditor().escSeq}${ColourEditor().setText}m"
				val oldBorder = "${ColourEditor().escSeq}${ColourEditor().setBorder}m"
				val oldBorderText = "${ColourEditor().escSeq}${ColourEditor().setTable}m"
            	colours.setColour()
            	val newText = "${ColourEditor().escSeq}${ColourEditor().setText}m"
				val newBorder = "${ColourEditor().escSeq}${ColourEditor().setBorder}m"
				val newBorderText = "${ColourEditor().escSeq}${ColourEditor().setTable}m"
            	tasks.tasks.clear()
        		val tempTasks = mutableListOf<String>()
        		for (i in tasks.storeJson){
            		tempTasks.add(i!!.task)
            	}
            	for (i in tempTasks){
            		tasks.tasks.add(PrintOut().taskLayout(i))
        		}
        		for (i in tasks.tasksTimeDatePriority.indices){
    				tasks.tasksTimeDatePriority[i] = tasks.tasksTimeDatePriority[i].replace(oldText, newText)
    				tasks.tasksTimeDatePriority[i] = tasks.tasksTimeDatePriority[i].replace(oldBorder, newBorder)
    				tasks.tasksTimeDatePriority[i] = tasks.tasksTimeDatePriority[i].replace(oldBorderText, newBorderText)
        		}
        		tempTasks.clear()
            }
            // Delete a Task
            userInput == "5" -> {
            	clearScreen()
            	print.table()
                print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
                tasks.deleteTask()
            }
            // Edit Mode
            userInput == "4" -> {
            	clearScreen()
            	print.table()
                print.printTasks(tasks.tasksTimeDatePriority, tasks.tasks)
                tasks.editTask()
            }
            // Quit
            userInput == "6" -> {
                println("\nGoodbye.")
                if (tasks.storeJson.isNotEmpty()){
                	savedList.saveJson(tasks.storeJson)
                }
                exitProcess(0)
            }
            else -> {
		        println("\nThe input action is invalid.")
                println("Press Enter To Continue...")
                readln()
            }
        }
    }
}
