package io.arunbuilds.instagramclient.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.arunbuilds.instagramclient.InstagramClient
import io.arunbuilds.instagramclient.R
import io.arunbuilds.instagramclient.home.adapter.UserDataListAdapter
import io.arunbuilds.instagramclient.home.adapter.load
import io.arunbuilds.instagramclient.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {

    lateinit var userDataListAdapter: UserDataListAdapter
    private val userlist = listOf<UserData>()
    lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = ViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, factory).get(HomeActivityViewModel::class.java)


        userDataListAdapter =
            UserDataListAdapter(userlist) {
                showMaterialDialog(it)
            }
        rvList.layoutManager = LinearLayoutManager(this).also {
            it.reverseLayout = true
            it.stackFromEnd = true
        }
        rvList.adapter = userDataListAdapter
        rvList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            viewModel.loadfromDb()
            swiperefresh.isRefreshing = false
        }

        viewModel.mutableListLiveData.observe(this, Observer {
            ivError.visibility = View.GONE
            userDataListAdapter.updateDataSet(it)
        })

        viewModel.errorLiveData.observe(this, Observer {
            ivError.visibility = View.VISIBLE
            Toast.makeText(this, "Error Occured ", Toast.LENGTH_LONG).show()
        })


        WorkManager.getInstance(this).getWorkInfosByTagLiveData(InstagramClient.UNIQUE_TAG)
            .observe(this,
                Observer {
                    it.forEach {

                        Log.d("HomeActivity", it.state.toString())
                    }


                })

    }

    private fun showMaterialDialog(data: UserData) {
        val dialog = MaterialDialog(this)
            .customView(R.layout.layout_fullview, scrollable = true)

        val dp = dialog.getCustomView().findViewById<ImageView>(R.id.fullprofilepic)
        val bio = dialog.getCustomView().findViewById<TextView>(R.id.bioText)
        val datePosted = dialog.getCustomView().findViewById<TextView>(R.id.dataPosted)

        val shareBio = dialog.getCustomView().findViewById<Button>(R.id.shareBioButton)
        val shareDp = dialog.getCustomView().findViewById<Button>(R.id.shareImageButton)


        dp.load(data.userprofilepic)
        bio.text = data.userbio
        datePosted.text = SimpleDateFormat("dd MMM yyyy", Locale.US).format(data.date.time).toString()


        shareBio.setOnClickListener {
            shareText("${data.username} has updated bio as ${data.userbio}")
        }


        shareDp.setOnClickListener {
            shareText("${data.username} has updated profile pic \n Check it out here: ${data.userprofilepic}")
        }


        dialog.show {
            cornerRadius(16f)
        }
    }

    private fun shareText(value: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, value)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
    }
}
