package io.arunbuilds.instagramclient

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.arunbuilds.instagramclient.model.Graphql
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.jsoup.Jsoup


class MainActivity : AppCompatActivity() {
    val subss: CompositeDisposable = CompositeDisposable()
    val TAG = MainActivity::class.java.name.toString()
    val url = "https://instagram.com/arunm619"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fetchButton.setOnClickListener {
            scrapData()
        }

    }

    private fun scrapData() {
        getDatafromInstagram()
    }

    private fun updateViews(data: Graphql) {
        tvData.text = data.user.toString()
    }

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
                    val p = g.fromJson(data.getJSONObject("graphql").toString(), Graphql::class.java)
                    p
                   }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateViews(it)



                    Log.d(TAG, it.toString())


                }, {
                    Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show()

                    Log.d(TAG, "error - ${it.cause} ${it.localizedMessage}")
                }, {

                    Toast.makeText(this, "Everything success", Toast.LENGTH_LONG).show()
                })

        subss.add(disposable)

    }

    override fun onDestroy() {
        super.onDestroy()
        subss.dispose()
    }
}
