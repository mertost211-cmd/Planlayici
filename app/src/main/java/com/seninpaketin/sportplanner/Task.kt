package com.seninpaketin.sportplanner

data class Task(
    val id: Int,
    val title: String,
    val hour: Int,
    val minute: Int,
    val repeatType: String = "DAILY", // "DAILY", "WEEKLY", "MONTHLY"
    val weekDays: List<Int> = emptyList(), // Calendar.SUNDAY(1)..SATURDAY(7)
    val dayOfMonth: Int = 1
)
