package io.arunbuilds.instagramclient.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.arunbuilds.instagramclient.Db.UserAppDatabase
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HomeActivityViewModel constructor(
    application: Application
) : AndroidViewModel(application) {
    val TAG = "Viewmodel"
    val subs: CompositeDisposable = CompositeDisposable()

    val mutableListLiveData: MutableLiveData<List<UserData>> = MutableLiveData()
    val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    init {
        loadfromDb()
    }

    private fun testAddUserintoDb(n: Int) {
        for (i in 1..n) {
            val userData = UserData(
                id = 0,
                username = "Arun$i",
                userprofilepic = "hello",
                date = Date(),
                userbio = "Bio data $i"
            )
            UserAppDatabase.getInstance(getApplication()).userdataDAO.addUserData(userData)
        }


    }

     fun loadfromDb() {

        val d = UserAppDatabase.getInstance(getApplication()).userdataDAO.getUserData()
            .subscribeOn(Schedulers.io())
            .doOnNext {
                it.forEach {
                    Log.d(TAG, it.username + "    " + it.id)
                }
            }
            .subscribe({
                mutableListLiveData.postValue(it)
            }, {
                errorLiveData.postValue(it)
            })

        subs.add(d)

    }
}