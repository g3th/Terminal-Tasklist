package tasklist
import java.io.*
import kotlin.system.exitProcess

class ColourEditor {
	val escSeq = "\u001B[38;5;"
	lateinit var setB: String
	lateinit var setT: String
	lateinit var setTT: String
	var setBorder: Int? = null
	var setText: Int? = null
	var setTableText: Int? = null
	var gradientStart: Int? = null
	val colorScheme = File("colorScheme")
	lateinit var colorSchemeContent: List<String>
		
	init {	
		if (colorScheme.exists()){
			try {
				colorSchemeContent = colorScheme.readLines()
				gradientStart = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
				setBorder = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
				setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
				setTableText = colorSchemeContent[2].split(";")[2].replace("m","").toInt()
				setB = setBorder.toString()
				setT = setText.toString()
				setTT = setB.toString()
			} catch (e: NumberFormatException){
				clearScreen()
				println("The color scheme file is invalid. \nPlease delete it and start again\n")
				println("Ending.\n")
				exitProcess(0)
			}
		} else {
			clearScreen()
			PrintOut().plainHeader()
			setColour()
		}
	}
	
	fun reformatColours(tasksTimeDatePriority: String, tempStore: TaskList.ReformatColours): String {
		val ttdp = tasksTimeDatePriority
				.replace(tempStore.date, "${escSeq}${setText}m${tempStore.date}${escSeq}${setTableText}m")
				.replace(tempStore.time, "${escSeq}${setText}m${tempStore.time}${escSeq}${setTableText}m")
				.replace(tempStore.priority, "${escSeq}${setText}m${tempStore.priority}${escSeq}${setTableText}m")
				.replace(tempStore.due, "${escSeq}${setText}m${tempStore.due}${escSeq}${setTableText}m")

		return ttdp
	}
	
	fun setColour(){

		top@while(true) {
			if (gradientStart != null) {
				clearScreen()
				PrintOut().table()
				colorMatrixPrintout()
				println("\n\n${escSeq}${setText}mInput your chosen colour scheme:\n")
				println("1. Main Text Colour")
				println("2. Table Text Colour")
				println("3. Table Borders Colour")
				println("4. End")
				print("> ")
				val validInput = listOf("1","2","3","4")			
				try {
					val input = readln()
					if (validInput.all { it != input } ){
						println("Invalid input")
						println("Press Enter to Continue")
						readln()
					} else {
		 				when (input) {			
							"1" -> {
								clearScreen()
								PrintOut().table()
								colorMatrixPrintout()
								print("\n\n${escSeq}${setText}mMain Text Colour (0 - 255): > ")
								setT = readln()
							}							
							"2" -> {
								clearScreen()
								PrintOut().table()
								colorMatrixPrintout()
								print("\n\n${escSeq}${setText}mTable Text Colour (0 - 255): > ")
								setTT = readln()
							}							
							"3" -> {
								clearScreen()
								PrintOut().table()
								colorMatrixPrintout()
								print("\n\n${escSeq}${setText}mTable Border Colour (0 - 255): > ")
								setB = readln()
							}
							"4" -> {
								saveColours()
								println(setT)
								println(setB)
								println(setTT)
								readln()
								break@top
							}
						}
						if (setB.toInt() < 0 || setB.toInt() > 255 
							|| setT.toInt() < 0 || setT.toInt() > 255 
							|| setTT.toInt() < 0 || setTT.toInt() > 255){
							println("One of the values entered was invalid.")
							println("Press Enter to Continue...")
							readln()
						}
					}
				} catch (e: NumberFormatException){
					println("One of the values entered is not a number")
					readln()
				}		
		} else {
			gradientStart = 34
			setBorder = 34
			setText = 34
			setTableText = 34
			println("Table color scheme not found,")
			println("so I set a default colour scheme.")
			println("\nPress Enter to Continue...")
			saveColours()
			readln()
			break
			}
		}
	}
	
	fun saveColours(){
		colorScheme.delete()
		colorScheme.writeText("${escSeq}${setB}m\n")
		colorScheme.appendText("${escSeq}${setT}m\n")
		colorScheme.appendText("${escSeq}${setTT}m")
		colorSchemeContent = colorScheme.readLines()
		gradientStart = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
		setBorder = colorSchemeContent[0].split(";")[2].replace("m","").toInt()
		setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
		setTableText = colorSchemeContent[2].split(";")[2].replace("m","").toInt()
	}
	
	fun colorMatrixPrintout(){
    	val matrix = listOf(
		    (0..255 step 16).toList(), (1..255 step 16).toList(), (2..255 step 16).toList(),
		    (3..255 step 16).toList(), (4..255 step 16).toList(), (5..255 step 16).toList(),
		    (6..255 step 16).toList(), (7..255 step 16).toList(), (8..255 step 16).toList(),
		    (9..255 step 16).toList(), (10..255 step 16).toList(), (11..255 step 16).toList(),
		    (12..255 step 16).toList(), (13..255 step 16).toList(), (13..255 step 16).toList(),
		    (14..255 step 16).toList(), (15..255 step 16).toList()
    	)
		println("\u001B[38;5;255mCurrent Colour Scheme:")
		println("\u001B[38;5;255mTable Text:${escSeq}${setTableText}m█ Table Border:${escSeq}${setBorder}m█ Main Text:${escSeq}${setText}m█")
		//setBorder: Int? = null
		for (i in matrix) {
		    println()
		    for (j in i) {
		        if (j < 10) {
		            print("\u001B[38;5;255m$j  \u001B[48;5;${j}m  \u001B[0m ")
		        } else if (j in 10..99){
		            print("\u001B[38;5;255m$j \u001B[48;5;${j}m  \u001B[0m ")
		        } else {
		            print("\u001B[38;5;255m$j\u001B[48;5;${j}m  \u001B[0m ")
		        }
		    }
		}
	}
}
