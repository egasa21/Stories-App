package com.lazzy.stories.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lazzy.stories.data.remote.response.ListStoryItem


@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stories: List<Stories>)

    @Query("SELECT * FROM Stories")
    fun getStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM Stories")
    suspend fun deleteAll()
}