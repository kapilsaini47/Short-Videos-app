package com.example.shortvideos.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shortvideos.network.NetworkManager
import com.example.shortvideos.repository.Repository

class VideoViewModelFactory(
    private val repository: Repository,
    private val application:Application,
    private val networkManager: NetworkManager
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoViewModel(repository,application,networkManager) as T
    }
}