package com.seninpaketin.sportplanner

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.seninpaketin.sportplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askNotificationPermission()

        adapter = TaskAdapter(mutableListOf()) { task ->
            showDeleteDialog(task)
        }

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = adapter

        binding.addTaskButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTaskList()
    }

    private fun refreshTaskList() {
        val tasks = TaskStorage.getTasks(this)
        adapter.updateTasks(tasks)
        binding.emptyText.visibility =
            if (tasks.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun showDeleteDialog(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(task.title)
            .setMessage("Bu görevi silmek istiyor musun?")
            .setPositiveButton("Sil") { _, _ ->
                AlarmScheduler.cancelAlarm(this, task.id)
                TaskStorage.deleteTask(this, task.id)
                refreshTaskList()
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101
                )
            }
        }
    }
}
