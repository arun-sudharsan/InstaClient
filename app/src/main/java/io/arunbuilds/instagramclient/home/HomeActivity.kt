package io.arunbuilds.instagramclient.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.arunbuilds.instagramclient.R
import io.arunbuilds.instagramclient.home.adapter.UserDataListAdapter
import io.arunbuilds.instagramclient.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


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
            UserDataListAdapter(userlist)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = userDataListAdapter


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


    }



}
