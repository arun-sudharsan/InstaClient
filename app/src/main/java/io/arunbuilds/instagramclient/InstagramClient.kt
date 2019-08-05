package io.arunbuilds.instagramclient

import android.app.Application
import android.util.Log
import androidx.work.*
import io.arunbuilds.instagramclient.workmanager.FetchDataWorker
import java.util.concurrent.TimeUnit


class InstagramClient : Application() {

    companion object {
        val TAG: String = InstagramClient::class.java.name
        val UNIQUE_TAG: String = TAG
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "OnCreate Of InstagramClient")

        scheduleWork(UNIQUE_TAG)

    }


    fun scheduleWork(tag: String) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val fetchDataWorker =
            PeriodicWorkRequest.Builder(FetchDataWorker::class.java, 15, TimeUnit.MINUTES)
        val request = fetchDataWorker.setConstraints(constraints).build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}