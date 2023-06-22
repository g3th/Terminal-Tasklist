package tasklist

import java.io.File
import kotlinx.datetime.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class TasksToJson(val date: String, val time: String, val priority: String, val dueTag: String, val task: String)

class SaveReadJsonFile {

    private val file = File("save/tasklist.json")
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val tasksAdapter = moshi.adapter(TasksToJson::class.java)

    fun formatJson(date: String, time: String, priority: String, colourTag: String, task: String): TasksToJson?{
        val jsonTasks = TasksToJson(date = date,
            time = time.split("T")[1],
            priority = priority,
            dueTag = colourTag,
            task = task)
        return jsonTasks
    }

    fun readIt(tasks: MutableList<TasksToJson?>) {
        val jsonStrings = mutableListOf<String>()
        for (i in file.readLines().indices) {
            jsonStrings.add(file.readLines()[i])
        }
        for (i in jsonStrings.indices) {
            if (i == 0) {
                continue
            } else if (i == jsonStrings.lastIndex) {
                continue
            } else {
                tasks.add(
                    tasksAdapter.fromJson(
                        jsonStrings[i]
                            .replace("},", "}")
                    )
                )
            }
        }
    }

    data class FilledTasks (val timeDate: MutableList<String>, val listOfTasks: MutableList<String>, )
    fun readJsonAndFillTasks(timedate: MutableList<String>, jsonTasks: MutableList<TasksToJson?>): FilledTasks {
        var r = mutableListOf<String>()
        for (i in jsonTasks.indices) {
            timedate.add(
                "${
                    LocalDate(
                        jsonTasks[i]!!.date.split("-")[0].toInt(),
                        jsonTasks[i]!!.date.split("-")[1].toInt(),
                        jsonTasks[i]!!.date.split("-")[2].toInt()
                    )
                } " +
                        "| ${jsonTasks[i]!!.time}" +
                        " | ${jsonTasks[i]!!.priority} | ${jsonTasks[i]!!.dueTag}"
            )
            r.add(jsonTasks[i]!!.task)
        }
        return FilledTasks(timedate, r)
    }

    fun saveJson(list: MutableList<TasksToJson?>){
        file.writeText("[ \n")
        for (i in list.indices) {
            if (i == list.size -1){
                file.appendText(tasksAdapter.toJson(list[i]))
                .also { file.appendText("\n") }
            } else {
                file.appendText(tasksAdapter.toJson(list[i]))
                .also { file.appendText(",\n") }
            }
        }
        file.appendText("]")
    }
}
