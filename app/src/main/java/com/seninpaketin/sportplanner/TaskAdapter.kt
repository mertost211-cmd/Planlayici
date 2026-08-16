package com.seninpaketin.sportplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.taskTimeText)
        val titleText: TextView = view.findViewById(R.id.taskTitleText)
        val repeatText: TextView = view.findViewById(R.id.taskRepeatText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.timeText.text = String.format("%02d:%02d", task.hour, task.minute)
        holder.titleText.text = task.title
        holder.repeatText.text = repeatLabel(task)

        holder.itemView.setOnLongClickListener {
            onTaskLongClick(task)
            true
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: MutableList<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    private fun repeatLabel(task: Task): String {
        return when (task.repeatType) {
            "WEEKLY" -> {
                val names = mapOf(
                    Calendar.MONDAY to "Pzt", Calendar.TUESDAY to "Sal",
                    Calendar.WEDNESDAY to "Çar", Calendar.THURSDAY to "Per",
                    Calendar.FRIDAY to "Cum", Calendar.SATURDAY to "Cmt",
                    Calendar.SUNDAY to "Paz"
                )
                task.weekDays.sorted().joinToString(", ") { names[it] ?: "" }
            }
            "MONTHLY" -> "Her ayın ${task.dayOfMonth}. günü"
            else -> "Her gün"
        }
    }
}
