package com.example.shortvideos.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.shortvideos.model.ResponseModel
import com.example.shortvideos.network.NetworkManager
import com.example.shortvideos.repository.Repository
import com.example.shortvideos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class VideoViewModel(
    private val repository: Repository,
    applicationContext:Application,
    private val networkManager: NetworkManager
):AndroidViewModel(applicationContext) {

    //mutable live data of response received from api
    private val _response: MutableLiveData<Resource<ResponseModel>> = MutableLiveData()
    //only live data to be observe in activity so it can't be modified from outside the view-model
    val response: LiveData<Resource<ResponseModel>>
    get() = _response

    init {
        getVideos("yellow+flower")
    }

    private fun getVideos(q:String){
        //launching coroutine to call suspend function from repository
        viewModelScope.launch {
          handleNetworkSafeGetVideos(q)
        }
    }

    //Api response safe method
    private fun handleGetVideos(response: Response<ResponseModel>): Resource<ResponseModel> {
        if (response.isSuccessful){
            response.body().let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message().toString())
    }

    //Network safe method
    private suspend fun handleNetworkSafeGetVideos(q: String){
        _response.postValue(Resource.Loading())
        try {
            if (networkManager.hasInternetConnection(getApplication<Application>().applicationContext)){
                val response = repository.getVideos(q)
                _response.postValue(handleGetVideos(response))
            }else{
                _response.postValue(Resource.Error("No internet connection"))
            }
        }catch (e:Throwable){
            when(e){
                is IOException -> _response.postValue(Resource.Error("Network Failure"))
                else -> _response.postValue(Resource.Error(e.message.toString()))
            }
        }
    }



}