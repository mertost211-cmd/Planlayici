package com.seninpaketin.sportplanner

import android.app.DatePickerDialog
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
    private var selectedMonthDay = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickTimeButton.setOnClickListener {
            showTimePicker()
        }

        binding.pickMonthDayButton.setOnClickListener {
            showMonthDayPicker()
        }

        binding.repeatTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioWeekly -> {
                    binding.weekDaysLayout.visibility = android.view.View.VISIBLE
                    binding.monthDayLayout.visibility = android.view.View.GONE
                }
                R.id.radioMonthly -> {
                    binding.weekDaysLayout.visibility = android.view.View.GONE
                    binding.monthDayLayout.visibility = android.view.View.VISIBLE
                }
                else -> {
                    binding.weekDaysLayout.visibility = android.view.View.GONE
                    binding.monthDayLayout.visibility = android.view.View.GONE
                }
            }
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

    private fun showMonthDayPicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, _, _, dayOfMonth ->
                selectedMonthDay = dayOfMonth
                binding.selectedMonthDayText.text = dayOfMonth.toString()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getSelectedWeekDays(): List<Int> {
        val days = mutableListOf<Int>()
        if (binding.checkMon.isChecked) days.add(Calendar.MONDAY)
        if (binding.checkTue.isChecked) days.add(Calendar.TUESDAY)
        if (binding.checkWed.isChecked) days.add(Calendar.WEDNESDAY)
        if (binding.checkThu.isChecked) days.add(Calendar.THURSDAY)
        if (binding.checkFri.isChecked) days.add(Calendar.FRIDAY)
        if (binding.checkSat.isChecked) days.add(Calendar.SATURDAY)
        if (binding.checkSun.isChecked) days.add(Calendar.SUNDAY)
        return days
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

        val repeatType = when (binding.repeatTypeGroup.checkedRadioButtonId) {
            R.id.radioWeekly -> "WEEKLY"
            R.id.radioMonthly -> "MONTHLY"
            else -> "DAILY"
        }

        val weekDays = if (repeatType == "WEEKLY") getSelectedWeekDays() else emptyList()

        if (repeatType == "WEEKLY" && weekDays.isEmpty()) {
            Toast.makeText(this, "En az bir gün seç", Toast.LENGTH_SHORT).show()
            return
        }

        val dayOfMonth = if (repeatType == "MONTHLY") selectedMonthDay else 1

        val task = TaskStorage.addTask(
            this, title, selectedHour, selectedMinute, repeatType, weekDays, dayOfMonth
        )
        AlarmScheduler.scheduleAlarm(this, task)

        Toast.makeText(this, "Görev eklendi", Toast.LENGTH_SHORT).show()
        finish()
    }
}
