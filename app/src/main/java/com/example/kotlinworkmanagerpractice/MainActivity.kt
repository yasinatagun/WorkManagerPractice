package com.example.kotlinworkmanagerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Time
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.sql.Ref
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey", 1).build()
        val constraints = Constraints.Builder()
            // .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

//        val myWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
//            .setConstraints(constraints)
//            .setInputData(data)
////            .setInitialDelay(5, TimeUnit.SECONDS)
////            .addTag("myTag")
//            .build()

        // DOCUMENTTA SÖYLÜYOR. ANDROİD 15 DAKİKAYA İZİN VERİYOR.
        // 15 DAKİKADAN ÖNCE İSE HANDLER RUNNER TARZI ŞEYLER KULLANMAYI ÖNERİYOR.
        val myWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)

        // Observe etmek
        WorkManager.getInstance(this).getWorkInfoById(myWorkRequest.id)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id)
            .observe(this, Observer {
                if (it.state == WorkInfo.State.RUNNING) {
                    println("running bro!")
                } else if (it.state == WorkInfo.State.FAILED) {
                    println("failed bro!")
                } else if (it.state == WorkInfo.State.SUCCEEDED) {
                    println("succeeded bro!")
                }
            })

        // WORK CANCELING
//        WorkManager.getInstance(this).cancelWorkById(myWorkRequest.id)

        // CHANINING
        // SADECE ONE TIME REQUESTLERDE SIRA SIRA VS. ÇALIŞTIRILABİLİR.

        val oneTimeWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<RefreshDatabase>().setConstraints(constraints)
                .setInputData(data).build()

        WorkManager.getInstance(this)
            .beginWith(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .enqueue()


    }
}