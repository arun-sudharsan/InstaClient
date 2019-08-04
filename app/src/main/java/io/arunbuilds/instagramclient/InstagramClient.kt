package io.arunbuilds.instagramclient

import android.app.Application
import android.util.Log
import androidx.work.WorkManager
import io.arunbuilds.instagramclient.Db.UserAppDatabase
import androidx.work.OneTimeWorkRequest
import io.arunbuilds.instagramclient.workmanager.FetchDataWorker


class InstagramClient : Application() {
    val TAG = InstagramClient::class.java.name
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "OnCreate Of InstagramClient")

        val oneshotwork: OneTimeWorkRequest = OneTimeWorkRequest.Builder(FetchDataWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(oneshotwork)
    }
}