package tasklist

import java.io.*

data class tasks (val tasksTDP: List<String>, val task: List<String>)

class PrintOut {
	
	lateinit var setBorder: String
	lateinit var setText: String
	lateinit var header_one: String
	lateinit var header_two: String
	lateinit var header_three: String
	lateinit var header_bottom: String
	private val colorScheme = File("colorScheme")
	private val colorSchemeContent = colorScheme.readLines()
	
	fun table(){
		if (colorScheme.exists()){
			val escSeq = "\u001B[38;5;"
			val gradientStart = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
			setBorder = colorSchemeContent[0]
			setText = colorSchemeContent[1]
			println("${escSeq}${gradientStart}m████████╗███████╗██████╗ ███╗   ███╗██╗███╗   ██╗ █████╗ ██╗        ████████╗ █████╗ ███████╗██╗  ██╗██╗     ██╗███████╗████████╗")
			println("${escSeq}${gradientStart+36}m╚══██╔══╝██╔════╝██╔══██╗████╗ ████║██║████╗  ██║██╔══██╗██║        ╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║     ██║██╔════╝╚══██╔══╝")
			println("${escSeq}${gradientStart+72}m   ██║   █████╗  ██████╔╝██╔████╔██║██║██╔██╗ ██║███████║██║           ██║   ███████║███████╗█████╔╝ ██║     ██║███████╗   ██║")
			println("${escSeq}${gradientStart+108}m   ██║   ██╔══╝  ██╔══██╗██║╚██╔╝██║██║██║╚██╗██║██╔══██║██║           ██║   ██╔══██║╚════██║██╔═██╗ ██║     ██║╚════██║   ██║")
			println("${escSeq}${gradientStart+144}m   ██║   ███████╗██║  ██║██║ ╚═╝ ██║██║██║ ╚████║██║  ██║███████╗      ██║   ██║  ██║███████║██║  ██╗███████╗██║███████║   ██║")
			println("${escSeq}${gradientStart+180}m   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝      ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝   ╚═╝")
			println("------------------------------------------------------------------------------------------------------------------------------")					
			header_one = "${setBorder}+----+------------+-------+---+---+--------------------------------------------+"
			header_two = "${setBorder}| ${setText}N${setBorder}  |    ${setText}Date${setBorder}    | ${setText}Time${setBorder}  | ${setText}P${setBorder} | ${setText}D${setBorder} |                   ${setText}Task${setBorder}                     |"
			header_three = "${setBorder}+----+------------+-------+---+---+--------------------------------------------+"
			header_bottom = "\n${setBorder}+----+------------+-------+---+---+--------------------------------------------+"
		} else {
			println("Table color scheme not found. Please set Colours before starting:")
			setColour()
			colorScheme.writeText(setBorder+"\n")
			colorScheme.appendText(setText)
		}
	}
	
    fun setColour(){
		println("Enter Border color (0 - 255):")
		val setB = readln()
		println("Enter Text color (0 - 255):")
		val setT = readln()
		setBorder = "\u001B[38;5;${setB}m"
		setText = "\u001B[38;5;${setT}m"
		colorScheme.delete()
		colorScheme.writeText(setBorder + "\n")
		colorScheme.appendText(setText)
	}

    fun taskLayout(input: String): String {
        var printoutInput = ""
        val strList = input.split("\n")
        var chunkedList = mutableListOf<String>()
		setBorder = colorSchemeContent[0]
		setText = colorSchemeContent[1]
        for (i in strList) {
            chunkedList += i.chunked(44)
        }
        for(i in chunkedList.indices){
            if (chunkedList[i].length < 44) {
                if (i == 0){
                    printoutInput += "${setBorder}${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}|"

                } else {
                    printoutInput += "\n${setBorder}|    |            |       |   |   |${setText}${chunkedList[i]} ${" ".repeat(43 - chunkedList[i].length)}${setBorder}|"
                }
            } else {
                if (i == 0){
                    printoutInput += "${setText}${chunkedList[i]}${setBorder}|"
                } else {
                    printoutInput += "\n${setBorder}|    |            |       |   |   |${setText}${chunkedList[i]}${setBorder}|"
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
                print("|${setText} $counter${setBorder}  | ${setText}${a[c]} ${setBorder}|${setText}${b[c]}")
                println(header_bottom)
                counter++
            }
        }
        println()
    }
}
