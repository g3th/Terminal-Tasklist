package tasklist
import kotlin.system.*

import java.io.*
import kotlin.system.exitProcess
data class tasks (val tasksTDP: List<String>, val task: List<String>)

class PrintOut {
	lateinit var tableBorder: String
	lateinit var tableElements: String
	/* 	The header below will get called if there is no 'ColorScheme" file present.
		"Why not use the standard table?" You may ask. Because the standard table causes a StackOverFlow error.
		"Why does it cause an error?" You may ask. Because 'ColourEditor()' is invoked (I think?), and therefore
		'PrintOut().table()' can't be invoked in the ColourEditor class without causing endless recursion.
		"Why do you need to invoke ColourEditor()?". You may ask. So that colours are refreshed in real-time,
		 when the user sets them in the program, without necessitating any restarts.
		 "Why, then, not...". Go to bed now, take your medicine.
	*/
	fun plainHeader(){
		println("\u001B[38;5;231m------------- Fully Editable - Fully Colourful ---------------\n")
		println("████████╗ █████╗ ███████╗██╗  ██╗██╗     ██╗███████╗████████╗")
		println("╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║     ██║██╔════╝╚══██╔══╝")
		println("   ██║   ███████║███████╗█████╔╝ ██║     ██║███████╗   ██║")
		println("   ██║   ██╔══██║╚════██║██╔═██╗ ██║     ██║╚════██║   ██║")
		println("   ██║   ██║  ██║███████║██║  ██╗███████╗██║███████║   ██║")
		println("   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝   ╚═╝\n")
		println("----------------- https://github.com/g3th -------------------\n")
	}
	
	// The 'Standard' Table. Read above.
	fun table(){
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart}m------------- Fully Editable - Fully Colourful ---------------\n")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart!!+36}m████████╗ █████╗ ███████╗██╗  ██╗██╗     ██╗███████╗████████╗")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart!!+72}m╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║     ██║██╔════╝╚══██╔══╝")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart!!+108}m   ██║   ███████║███████╗█████╔╝ ██║     ██║███████╗   ██║")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart!!+72}m   ██║   ██╔══██║╚════██║██╔═██╗ ██║     ██║╚════██║   ██║")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart!!+36}m   ██║   ██║  ██║███████║██║  ██╗███████╗██║███████║   ██║")
		println("${ColourEditor().escSeq}${ColourEditor().gradientStart}m   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝   ╚═╝\n")
		println("----------------- https://github.com/g3th -------------------\n")
		tableBorder = "${ColourEditor().escSeq}${ColourEditor().setBorder}m+----+------------+-------+---+---+--------------------------------------------+"
		tableElements = "${ColourEditor().escSeq}${ColourEditor().setBorder}m| ${ColourEditor().escSeq}${ColourEditor().setTableText}mN${ColourEditor().escSeq}${ColourEditor().setBorder}m  |    ${ColourEditor().escSeq}${ColourEditor().setTableText}mDate${ColourEditor().escSeq}${ColourEditor().setBorder}m    | ${ColourEditor().escSeq}${ColourEditor().setTableText}mTime${ColourEditor().escSeq}${ColourEditor().setBorder}m  | ${ColourEditor().escSeq}${ColourEditor().setTableText}mP${ColourEditor().escSeq}${ColourEditor().setBorder}m | ${ColourEditor().escSeq}${ColourEditor().setTableText}mD${ColourEditor().escSeq}${ColourEditor().setBorder}m |                   ${ColourEditor().escSeq}${ColourEditor().setTableText}mTask${ColourEditor().escSeq}${ColourEditor().setBorder}m                     |"
		}
		
	fun userOptions(){
		println("${ColourEditor().escSeq}${ColourEditor().setText}mInput an action:\n----------------")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m1. Add")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m2. Print")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m3. Set Colours")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m4. Edit")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m5. Delete")
		println("${ColourEditor().escSeq}${ColourEditor().setText}m6. End")					
	}
	
	/*	Format the task list:
		Divide the list into chunks of 44 characters, whether it has multiple elements (rows) or not.
		This is due to the fact that only a maximum of 44 characters fit a row in the "Tasks" column.
		
		Iterate through the chunks:
		If the element length is less than 44 characters, and there is only one element in the chunked list,
		it is only a single row. Therefore, append the correct amount of spaces to the length of the element
		with simple subtraction.
		
		If there is more than one element in the chunked list, but it is less than 44 characters, do the same,
		but add a newline and prepend the table to the task.
	*/
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
                    printoutInput += "${ColourEditor().escSeq}${ColourEditor().setText}m${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}${ColourEditor().escSeq}${ColourEditor().setBorder}m|"

                } else {
                    printoutInput += "\n|    |            |       |   |   |${ColourEditor().escSeq}${ColourEditor().setText}m${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}${ColourEditor().escSeq}${ColourEditor().setBorder}m|"
                }
            } else {
            	if (i == 0){
                    printoutInput += "${ColourEditor().escSeq}${ColourEditor().setText}m${chunkedList[i]}${ColourEditor().escSeq}${ColourEditor().setBorder}m|"
                } else {
                	printoutInput += "\n|    |            |       |   |   |${ColourEditor().escSeq}${ColourEditor().setText}m${chunkedList[i]}${ColourEditor().escSeq}${ColourEditor().setBorder}m|"
            		}
            	}
            }
        return printoutInput
    }
    fun printTasks(tasksDateTimePriority: MutableList<String>, tasks:MutableList<String>) {
        val tasklist = mutableListOf(tasks(tasksDateTimePriority, tasks))
        var counter = 1
		// Priority Legend
		print("${ColourEditor().escSeq}${ColourEditor().setText}mPriority: \u001B[38;5;196m█${ColourEditor().escSeq}${ColourEditor().setBorder}m = ${ColourEditor().escSeq}${ColourEditor().setBorder}mCritical | ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;226m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mHigh | ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;76m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mNormal | ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;45m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mLow\n\n")
		// Tag Legend
		print("${ColourEditor().escSeq}${ColourEditor().setText}mDue Tags: ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;196m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mOverdue | ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;76m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mComing Up Shortly | ")
		print("${ColourEditor().escSeq}${ColourEditor().setText}m\u001B[38;5;45m█${ColourEditor().escSeq}${ColourEditor().setText}m = ${ColourEditor().escSeq}${ColourEditor().setText}mIn Time\n\n")
        print("${tableBorder}\n${tableElements}\n${tableBorder}\n")
        // Print already formatted tasks
        for ((a,b) in tasklist) {
            for (c in a.indices) {
                print("| ${ColourEditor().escSeq}${ColourEditor().setTableText}m$counter${ColourEditor().escSeq}${ColourEditor().setBorder}m  | ${ColourEditor().escSeq}${ColourEditor().setText}${a[c]} ${ColourEditor().escSeq}${ColourEditor().setBorder}m|${b[c]}\n")
                println(tableBorder)
                counter++
            }
        }
        println()
    }
}
