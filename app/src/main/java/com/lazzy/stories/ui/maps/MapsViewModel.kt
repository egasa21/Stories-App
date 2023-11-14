package com.lazzy.stories.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lazzy.stories.data.StoriesRepository
import com.lazzy.stories.data.remote.response.ListStoryItem

class MapsViewModel (private val repository: StoriesRepository) : ViewModel(){

    fun getStoriesLocation(): LiveData<List<ListStoryItem>>{
        return repository.getStoriesLocation()
    }
}