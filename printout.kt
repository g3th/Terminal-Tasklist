package tasklist

import java.io.*
import kotlin.system.exitProcess
data class tasks (val tasksTDP: List<String>, val task: List<String>)

class PrintOut {
	val escSeq = "\u001B[38;5;"
	var setBorder: Int? = null
	var setText: Int? = null
	private var gradientStart: Int? = null
	private val colorScheme = File("colorScheme")
	private lateinit var colorSchemeContent: List<String>
	lateinit var header_one: String
	lateinit var header_two: String
	lateinit var header_three: String
	lateinit var header_bottom: String

	
	
	init {
		if (colorScheme.exists()){
			colorSchemeContent = colorScheme.readLines()
			gradientStart = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
			setBorder = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
			setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
		} else {
			clearScreen()
			setColour()
		}
	}
	
	fun table(){
		println("${escSeq}${gradientStart!!}m------ Fully Editable - Fully Colourful - Full o' Shit ------")
		println("${escSeq}${gradientStart!!+36}m████████╗ █████╗ ███████╗██╗  ██╗██╗     ██╗███████╗████████╗")
		println("${escSeq}${gradientStart!!+72}m╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║     ██║██╔════╝╚══██╔══╝")
		println("${escSeq}${gradientStart!!+108}m   ██║   ███████║███████╗█████╔╝ ██║     ██║███████╗   ██║")
		println("${escSeq}${gradientStart!!+144}m   ██║   ██╔══██║╚════██║██╔═██╗ ██║     ██║╚════██║   ██║")
		println("${escSeq}${gradientStart!!+108}m   ██║   ██║  ██║███████║██║  ██╗███████╗██║███████║   ██║")
		println("${escSeq}${gradientStart!!+72}m   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝   ╚═╝")
		println("----------------- https://github.com/g3th -------------------\n")
		header_one = "${escSeq}${setBorder}m+----+------------+-------+---+---+--------------------------------------------+"
		header_two = "${escSeq}${setBorder}m| ${escSeq}${setBorder!!+144}mN${escSeq}${setBorder}m  |    ${escSeq}${setBorder!!+144}mDate${escSeq}${setBorder}m    | ${escSeq}${setBorder!!+144}mTime${escSeq}${setBorder}m  | ${escSeq}${setBorder!!+144}mP${escSeq}${setBorder}m | ${escSeq}${setBorder!!+144}mD${escSeq}${setBorder}m |                   ${escSeq}${setBorder!!+144}mTask${escSeq}${setBorder}m                     |"
		header_three = "${escSeq}${setBorder}m+----+------------+-------+---+---+--------------------------------------------+"
		header_bottom = "\n${escSeq}${setBorder}m+----+------------+-------+---+---+--------------------------------------------+"
		}
		
	fun userOptions(){
		println("${escSeq}${setText!!}mInput an action:\n----------------")
		println("${escSeq}${setText!!+36}m1. Add")
		println("${escSeq}${setText!!+72}m2. Print")
		println("${escSeq}${setText!!+108}m3. Color")
		println("${escSeq}${setText!!+144}m4. Edit")
		println("${escSeq}${setText!!+108}m5. Delete")
		println("${escSeq}${setText!!+72}m6. End")					
	}
	
    fun setColour(){
    	while(true){
    		try{
    			if (gradientStart != null) {
    			 	table()
			 	} else {
			  		gradientStart = 34
  					setBorder = 34
					setText = 34
    			  	table()
    			  	println("\u001B[0m\nTable color scheme not found. Please set Colours before starting:")
			  	}
				println("Enter Border color (0 - 255):")
				val setB = readln()
				println("Enter Text color (0 - 255):")
				val setT = readln()
				if (setB.toInt() < 0 || setB.toInt() > 255 || setT.toInt() < 0 || setT.toInt() > 255){
					println("One of the values entered was invalid.")
					println("Press Enter to Continue...")
					readln()
				} else {
					colorScheme.delete()
					colorScheme.writeText("${escSeq}${setB}m\n")
					colorScheme.appendText("${escSeq}${setT}m")
					colorSchemeContent = colorScheme.readLines()
					gradientStart = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
					setBorder = gradientStart
					setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
					break				
				}
			} catch (e: NumberFormatException){
				println("One of the values entered is not a number")
				readln()
			}
		}
	}

    fun taskLayout(input: String): String {
        var printoutInput = ""
        val strList = input.split("\n")
        var chunkedList = mutableListOf<String>()
		setBorder = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
		setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
        for (i in strList) {
            chunkedList += i.chunked(44)
        }
        for(i in chunkedList.indices){
            if (chunkedList[i].length < 44) {
                if (i == 0){
                    printoutInput += "${escSeq}${setText}m${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}|"

                } else {
                    printoutInput += "\n${escSeq}${setBorder}m|    |            |       |   |   |${escSeq}${setText}m${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}${escSeq}${setBorder}m|"
                }
            } else {
                if (i == 0){
                    printoutInput += "${escSeq}${setText}m${chunkedList[i]}${escSeq}${setBorder}m|"
                } else {
                    printoutInput += "\n${escSeq}${setBorder}m|    |            |       |   |   |${escSeq}${setText}m${chunkedList[i]}${escSeq}${setBorder}m|"
                }
            }
        }
        return printoutInput
    }

    fun printTasks(tasksDateTimePriority: MutableList<String>, tasks:MutableList<String>) {
        val tasklist = mutableListOf(tasks(tasksDateTimePriority, tasks))
        var counter = 1
        print("${header_one}\n${header_two}\n${header_three}\n")
        for ((a,b) in tasklist) {
            for (c in a.indices) {
                print("|${escSeq}${setText!!}m $counter${escSeq}${setBorder}m  | ${escSeq}${setText}m${a[c]} ${escSeq}${setBorder}m|${escSeq}${setText}m${b[c]}")
                println(header_bottom)
                counter++
            }
        }
        println()
    }
}
