package com.lazzy.stories.di

import android.content.Context
import com.lazzy.stories.data.StoriesRepository
import com.lazzy.stories.data.local.dao.StoriesRoomDatabase
import com.lazzy.stories.data.remote.api.ApiConfig
import com.lazzy.stories.ui.preference.UserPreference

object Injection {
    fun provideRepository(context: Context, userPreference: UserPreference) : StoriesRepository {
        val database = StoriesRoomDatabase.getDatabase(context)
        val apiServices = ApiConfig.getApiServices(userPreference)
        return StoriesRepository(database, apiServices, userPreference)
    }
}