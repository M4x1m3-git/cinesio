//package com.example.flixyConnect.utils
//
//import android.content.Context
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//import androidx.work.workDataOf
//import com.example.flixyConnect.worker.ReleaseNotificationWorker
//import java.text.SimpleDateFormat
//import java.util.Locale
//import java.util.concurrent.TimeUnit
//
//object NotificationScheduler {
//
//    fun schedule(
//        context: Context,
//        movieId: Int,
//        title: String,
//        releaseDate: String
//    ) {
//        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val date = formatter.parse(releaseDate) ?: return
//
//        val delay = date.time - System.currentTimeMillis()
//        if (delay <= 0) return
//
//        val data = workDataOf("title" to title)
//
//        val request = OneTimeWorkRequestBuilder<ReleaseNotificationWorker>()
//            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
//            .setInputData(data)
//            .addTag("movie_$movieId")
//            .build()
//
//        WorkManager.getInstance(context).enqueue(request)
//    }
//
//    fun cancel(context: Context, movieId: Int) {
//        WorkManager.getInstance(context).cancelAllWorkByTag("movie_$movieId")
//    }
//}