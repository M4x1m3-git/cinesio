//package com.example.flixyConnect.worker
//
//import android.content.Context
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//
//class ReleaseNotificationWorker(
//    context: Context,
//    params: WorkerParameters
//) : Worker(context, params) {
//
//    override fun doWork(): Result {
//
//        val title = inputData.getString("title") ?: return Result.failure()
//
//        val notification = NotificationCompat.Builder(applicationContext, "releases_channel")
//            .setContentTitle("Sortie de film \uD83C\uDFAC")
//            .setContentText("$title est disponible !")
//            .setSmallIcon(android.R.drawable.ic_dialog_info)
//            .build()
//
//        NotificationManagerCompat.from(applicationContext)
//            .notify(System.currentTimeMillis().toInt(), notification)
//
//        return Result.success()
//    }
//}