package com.seninpaketin.sportplanner

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seninpaketin.sportplanner.databinding.ActivityAddTaskBinding
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var selectedHour = -1
    private var selectedMinute = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickTimeButton.setOnClickListener {
            showTimePicker()
        }

        binding.saveTaskButton.setOnClickListener {
            saveTask()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                binding.selectedTimeText.text = String.format("%02d:%02d", hour, minute)
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    private fun saveTask() {
        val title = binding.taskTitleInput.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Görev adını yaz", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedHour == -1) {
            Toast.makeText(this, "Saat seç", Toast.LENGTH_SHORT).show()
            return
        }

        val task = TaskStorage.addTask(this, title, selectedHour, selectedMinute)
        AlarmScheduler.scheduleAlarm(this, task)

        Toast.makeText(this, "Görev eklendi", Toast.LENGTH_SHORT).show()
        finish()
    }
}
