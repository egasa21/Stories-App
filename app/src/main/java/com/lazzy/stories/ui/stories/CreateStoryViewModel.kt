package com.lazzy.stories.ui.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lazzy.stories.data.remote.api.ApiConfig
import com.lazzy.stories.data.remote.response.AddStoryResponse
import com.lazzy.stories.data.remote.response.Story
import com.lazzy.stories.tools.Event
import com.lazzy.stories.ui.preference.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateStoryViewModel(private val userPreference: UserPreference) : ViewModel() {

    private val _stories = MutableLiveData<Story>()
    val stories: LiveData<Story> = _stories

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackbar: LiveData<Event<String>> = _snackBar

    private val _statusCode = MutableLiveData<Event<Int>>()
    val statusCode: LiveData<Event<Int>> = _statusCode

    fun createStories(
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ){
        _isLoading.postValue(Event(true))
        val client = ApiConfig.getApiServices(userPreference)
            .uploadImage(imageMultipart,description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.postValue(Event(false))
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        _snackBar.postValue(Event(responseBody.message))
                        _statusCode.postValue(Event(200))
                    }
                }else {
                    Log.d(TAG, "Response onFailure: ${response.message()}")
                    _snackBar.postValue(Event("Response onFailure: ${response.message()}"))
                    _statusCode.postValue(Event(400))
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.postValue(Event(false))
                Log.d(TAG, "onFailure: ${t.message}")
                _snackBar.postValue(Event("onFailure: ${t.message}"))
                _statusCode.postValue(Event(400))
            }
        })
    }

    companion object {
        private const val TAG = "CreateStoryViewModel"
    }
}