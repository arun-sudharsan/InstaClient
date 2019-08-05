package io.arunbuilds.instagramclient.workmanager

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import io.arunbuilds.instagramclient.Db.UserAppDatabase
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.arunbuilds.instagramclient.model.Graphql
import io.arunbuilds.instagramclient.notifications.Notifications.displayNotification
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*

const val url = "https://instagram.com/srimathikrishnan"
val TAG: String = FetchDataWorker::class.java.name
const val testing = false

class FetchDataWorker constructor(
    private val context: Context,
    workerParameters: WorkerParameters
) : RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
        return Single.just(true)
            .flatMap {
                getDatafromInstagram()
            }.map {
                savetoDb(it)
                Result.success()
            }.onErrorReturn { error ->
                Log.d(TAG, error.message)
                Result.retry()
            }
    }





    private fun getDatafromInstagram(): Single<Graphql> {
        return Single.just(true)
            .subscribeOn(Schedulers.io())
            .map {
                val jsonString = getRawJsonfromUrl(url)
                getGraphQLfromJson(jsonString)
            }
    }

    private fun getGraphQLfromJson(jsonString: String): Graphql? {
        Log.d(TAG, jsonString)
        val g = Gson()
        val data = JSONObject(jsonString)
        val p = g.fromJson(data.getJSONObject("graphql").toString(), Graphql::class.java)
        return p
    }


    private fun getRawJsonfromUrl(url: String): String {

        val document = Jsoup.connect(url).get()
        var rawJson = ""

        val scriptElements = document.getElementsByTag("script")

        for (element in scriptElements) {
            for (node in element.dataNodes()) {
                if (node.wholeData.startsWith("window._sharedData"))
                    rawJson = (node.wholeData)
            }
        }
        var result =
            rawJson.substring(
                (275)
                , rawJson.indexOf(",\"connected_fb_page\":null")
            )
        result = "{\"$result"
        result = "$result}}}"
        return result
    }


    private fun savetoDb(graphql: Graphql) {

        val userData = modelGraphQLtoUserData(graphql)

        if (userData.username == "username") {
            Log.d(TAG, "Empty user data discarded")
        } else {

            val d = UserAppDatabase.getInstance(applicationContext).userdataDAO.getLatestUserData()
                .map {

                    val newBio = it.userbio != userData.userbio
                    val newPic = it.userprofilepic != userData.userprofilepic
                    if (newBio || newPic) {
                        UserAppDatabase.getInstance(applicationContext).userdataDAO.addUserData(
                            userData
                        )
                        Log.d(TAG, "Saving because its new.")
                        buildNotifications(newBio, it)
                        it
                    } else {

                        if (testing) {
                            UserAppDatabase.getInstance(applicationContext).userdataDAO.addUserData(
                                userData
                            )
                        }
                        Log.d(TAG, "Not Saving because its old.")
                        it
                    }
                }.subscribe({

                    Log.d(TAG, "Success at saving ")
                }, {

                    if (it.message == "Query returned empty result set: select * from users where user_id = (SELECT MAX(user_id)  FROM users)") {
                        UserAppDatabase.getInstance(applicationContext).userdataDAO.addUserData(
                            userData
                        )
                    } else
                        Log.d(
                            TAG,
                            "error at fetching latest user beacuse it is  Message:${it.message}\n Cause:${it.cause}\n LocalizedMessage:${it.localizedMessage}"
                        )
                })

        }

    }

    private fun buildNotifications(
        newBio: Boolean,
        data: UserData
    ) {
        if (newBio) {
            displayNotification(context, "Bio Updated", data.userbio)
        } else {
            displayNotification(context, "Profile Picture Updated", "Click to view :P")
        }
    }


    private fun modelGraphQLtoUserData(data: Graphql): UserData {
        if (data.user == null) {
            return UserData()
        }
        return UserData(
            id = 0,
            username = data.user.username!!,
            userprofilepic = data.user.profile_pic_url_hd!!,
            date = Date(),
            userbio = data.user.biography!!
        )
    }
}