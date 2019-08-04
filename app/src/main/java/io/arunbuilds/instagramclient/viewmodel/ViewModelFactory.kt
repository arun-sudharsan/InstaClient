package io.arunbuilds.instagramclient.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.arunbuilds.instagramclient.home.HomeActivityViewModel

class ViewModelFactory constructor(
    application: Application
) : ViewModelProvider.Factory {

    private var application: Application = application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(application) as T
    }
}