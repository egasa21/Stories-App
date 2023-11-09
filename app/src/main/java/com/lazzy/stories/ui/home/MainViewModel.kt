package com.lazzy.stories.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lazzy.stories.data.StoriesRepository
import com.lazzy.stories.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(repository: StoriesRepository) : ViewModel() {
    private val userPreference = repository.userPreference

    val stories: LiveData<PagingData<ListStoryItem>> = repository.getStories().cachedIn(viewModelScope)

    fun logout(){
        viewModelScope.launch {
            userPreference.clearSession()
        }
    }
}