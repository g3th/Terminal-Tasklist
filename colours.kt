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
	var setTable: Int? = null
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
				setTable = colorSchemeContent[2].split(";")[2].replace("m","").toInt()
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
				.replace(tempStore.date, "${escSeq}${setText}m${tempStore.date}${escSeq}${setTable}m")
				.replace(tempStore.time, "${escSeq}${setText}m${tempStore.time}${escSeq}${setTable}m")
				.replace(tempStore.priority, "${escSeq}${setText}m${tempStore.priority}${escSeq}${setTable}m")
				.replace(tempStore.due, "${escSeq}${setText}m${tempStore.due}${escSeq}${setTable}m")

		return ttdp
	}
	
	fun setColour(){
		setB = "34"
		setT = "34"
		setTT = "34"
		while(true) {
			if (gradientStart != null) {
				println("Input your chosen colour scheme:\n")
				println("1. Border Colour")
				println("2. Text Colour")
				println("3. Table Borders Colour")
				print("> ")
				val validInput = listOf("1","2","3")			
				try {
					val input = readln()
					if (validInput.any { it != input } ){
						println("Invalid input")
					} else {
		 				when (input) {			
							"1" -> { 
								print("Table Colour (0 - 255): > ")
								setB = readln()
							}							
							"2" -> {
								print("Table Text Colour (0 - 255): > ")
								setT = readln()
							}							
							"3" -> {
								print("Table Border Colour (0 - 255): > ")
								setTT = readln()
							}
						}
						if (setB.toInt() < 0 || setB.toInt() > 255 
							|| setT.toInt() < 0 || setT.toInt() > 255 
							|| setTT.toInt() < 0 || setTT.toInt() > 255){
							println("One of the values entered was invalid.")
							println("Press Enter to Continue...")
							readln()
						} else {
							saveColours()
							break
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
		setBorder = gradientStart
		setText = colorSchemeContent[1].split(";")[2].replace("m","").toInt()
		setTable = colorSchemeContent[2].split(";")[2].replace("m","").toInt()
	}
	
	fun colorsLegend(){
		val col = 0
		print("\u001B38;5;${col}")
	}
}
