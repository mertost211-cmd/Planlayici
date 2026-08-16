package com.seninpaketin.sportplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onTaskLongClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeText: TextView = view.findViewById(R.id.taskTimeText)
        val titleText: TextView = view.findViewById(R.id.taskTitleText)
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
}
