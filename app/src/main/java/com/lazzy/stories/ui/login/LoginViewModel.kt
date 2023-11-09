package com.lazzy.stories.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lazzy.stories.data.local.UserModel
import com.lazzy.stories.data.remote.LoginBody
import com.lazzy.stories.data.remote.api.ApiConfig
import com.lazzy.stories.data.remote.response.AuthResponse
import com.lazzy.stories.tools.Event
import com.lazzy.stories.ui.preference.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreference: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading : LiveData<Event<Boolean>> = _isLoading

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackbar : LiveData<Event<String>> = _snackBar

    private val _isLogin = MutableLiveData<Event<Boolean>>()
    val isLogin : LiveData<Event<Boolean>> = _isLogin

    fun loginUser(loginBody: LoginBody) {
        _isLoading.value = Event(true)
        _isLogin.value = Event(false)

        val client = ApiConfig.getApiServices(userPreference).loginUser(loginBody)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    _isLogin.value = Event(true)

                    val loginResult = response.body()?.loginResult
                    val userModel = UserModel(
                        loginResult!!.name,
                        loginBody.email,
                        loginBody.password,
                        true,
                        loginResult.token
                    )

                    saveUser(userModel)

                    Log.d(TAG, "onSuccess: ${response.message()}")
                    _snackBar.value = Event("Login Success, ${response.message()}")
                }else{
                    _isLogin.value = Event(false)
                    Log.d(TAG, "Response onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = Event(false)
                _isLogin.value = Event(false)
                Log.d(TAG, "onFailure: ${t.message}")
                _snackBar.value = Event("onFailure: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun login(){
        viewModelScope.launch {
            userPreference.login()
        }
    }

    fun saveUser(userModel: UserModel){
        viewModelScope.launch {
            userPreference.saveUser(userModel)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}