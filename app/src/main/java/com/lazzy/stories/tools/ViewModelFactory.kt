package com.lazzy.stories.tools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lazzy.stories.di.Injection
import com.lazzy.stories.ui.detail.DetailViewModel
import com.lazzy.stories.ui.home.MainViewModel
import com.lazzy.stories.ui.login.LoginViewModel
import com.lazzy.stories.ui.preference.UserPreference
import com.lazzy.stories.ui.stories.CreateStoryViewModel

@Suppress("UNREACHABLE_CODE")
class ViewModelFactory(private val context: Context, private val userPreference: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(userPreference) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(Injection.provideRepository(context, userPreference)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(userPreference) as T
            }
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) ->{
                return CreateStoryViewModel(userPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class:" + modelClass.name )
        }
    }
}