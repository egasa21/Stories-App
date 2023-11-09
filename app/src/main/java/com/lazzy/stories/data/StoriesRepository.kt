package com.lazzy.stories.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.*
import com.lazzy.stories.data.local.dao.StoriesRoomDatabase
import com.lazzy.stories.data.remote.api.ApiServices
import com.lazzy.stories.data.remote.response.ListStoryItem
import com.lazzy.stories.ui.preference.UserPreference

class StoriesRepository (
    private val database: StoriesRoomDatabase,
    private val apiServices: ApiServices,
    val userPreference: UserPreference
    ) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config =  PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoriesRemoteMediator(database, apiServices),
            pagingSourceFactory = {
                database.storiesDao().getStories()
            }
        ).liveData
    }
}