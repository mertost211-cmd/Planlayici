package com.seninpaketin.sportplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val tasks = TaskStorage.getTasks(context)
            for (task in tasks) {
                AlarmScheduler.scheduleAlarm(context, task)
            }
        }
    }
}
