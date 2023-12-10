package com.example.kotlinworkmanagerpractice

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        val getData = inputData
        val num = getData.getInt("intKey", 0)
        refreshDatabase(num)
        return Result.success()
    }

    private fun refreshDatabase(num : Int) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "com.example.kotlinworkmanagerpractice",
            Context.MODE_PRIVATE
        )

        var savedNumber = sharedPreferences.getInt("myNumber", 0)
        savedNumber += num
        println(savedNumber)
        sharedPreferences.edit().putInt("myNumber", savedNumber).apply()
    }
}