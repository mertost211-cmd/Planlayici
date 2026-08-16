package com.seninpaketin.sportplanner

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object TaskStorage {
    private const val PREFS_NAME = "sport_planner_prefs"
    private const val KEY_TASKS = "tasks"

    fun getTasks(context: Context): MutableList<Task> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TASKS, "[]") ?: "[]"
        val array = JSONArray(json)
        val list = mutableListOf<Task>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val weekDaysArr = obj.optJSONArray("weekDays")
            val weekDays = mutableListOf<Int>()
            if (weekDaysArr != null) {
                for (j in 0 until weekDaysArr.length()) {
                    weekDays.add(weekDaysArr.getInt(j))
                }
            }
            list.add(
                Task(
                    id = obj.getInt("id"),
                    title = obj.getString("title"),
                    hour = obj.getInt("hour"),
                    minute = obj.getInt("minute"),
                    repeatType = obj.optString("repeatType", "DAILY"),
                    weekDays = weekDays,
                    dayOfMonth = obj.optInt("dayOfMonth", 1)
                )
            )
        }
        return list
    }

    fun saveTasks(context: Context, tasks: List<Task>) {
        val array = JSONArray()
        for (task in tasks) {
            val obj = JSONObject()
            obj.put("id", task.id)
            obj.put("title", task.title)
            obj.put("hour", task.hour)
            obj.put("minute", task.minute)
            obj.put("repeatType", task.repeatType)
            obj.put("weekDays", JSONArray(task.weekDays))
            obj.put("dayOfMonth", task.dayOfMonth)
            array.put(obj)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TASKS, array.toString()).commit()
    }

    fun addTask(
        context: Context,
        title: String,
        hour: Int,
        minute: Int,
        repeatType: String,
        weekDays: List<Int>,
        dayOfMonth: Int
    ): Task {
        val tasks = getTasks(context)
        val newId = (tasks.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(newId, title, hour, minute, repeatType, weekDays, dayOfMonth)
        tasks.add(newTask)
        saveTasks(context, tasks)
        return newTask
    }

    fun deleteTask(context: Context, taskId: Int) {
        val tasks = getTasks(context)
        tasks.removeAll { it.id == taskId }
        saveTasks(context, tasks)
    }
}
