package io.arunbuilds.instagramclient.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.arunbuilds.instagramclient.Db.UserAppDatabase
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.arunbuilds.instagramclient.model.Graphql
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*

public class MainActivityViewModel constructor(
    application: Application
) : AndroidViewModel(application) {
    val url = "https://instagram.com/arunm619"
    val TAG = "Viewmodel"
    val subs: CompositeDisposable = CompositeDisposable()
    var userAppDatabase: UserAppDatabase =
        Room.databaseBuilder(getApplication(), UserAppDatabase::class.java, "users.db")
            .build()

    val mutableListLiveData: MutableLiveData<List<UserData>> = MutableLiveData()
    val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    private fun getDatafromInstagram() {
        val disposable =
            Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map {
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
                    // val data = JSONObject(result)
                    val gson = GsonBuilder().create()

                    Log.d(TAG, result)

                    val g = Gson();
                    val data = JSONObject(result)
                    val p =
                        g.fromJson(data.getJSONObject("graphql").toString(), Graphql::class.java)
                    p
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    savetoDb(it)



                    Log.d(TAG, it.toString())


                }, {
                    //                    Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show()

                    Log.d(TAG, "error - ${it.cause} :::::::::::::::: ${it.localizedMessage}")
                }, {

                    //                  Toast.makeText(this, "Everything success", Toast.LENGTH_LONG).show()
                })

        //      subss.add(disposable)

    }

    init {
        loadfromDb()
    }


    @SuppressLint("CheckResult")
    private fun savetoDb(data: Graphql) {

        if (data.user != null) {
            val userData = UserData(
                id = 0,
                username = data.user.username!!,
                userprofilepic = data.user.profile_pic_url_hd!!,
                date = Date(),
                userbio = data.user.biography!!
            )
            userAppDatabase.userdataDAO.addUserData(userData)
        }
    }

    private fun loadfromDb() {
        val d = userAppDatabase.userdataDAO.getUserData()
            .subscribeOn(Schedulers.io())
            .subscribe({
                mutableListLiveData.postValue(it)
            }, {
                // Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show()
                errorLiveData.postValue(it)
            })

    }
}