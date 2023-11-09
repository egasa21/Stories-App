package com.lazzy.stories.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Stories::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)


abstract class StoriesRoomDatabase : RoomDatabase(){
 abstract fun storiesDao(): StoriesDao
 abstract fun remoteKeysDao():RemoteKeysDao

 companion object{
     @Volatile
     private var INSTANCE : StoriesRoomDatabase? = null

     @JvmStatic
     fun getDatabase(context: Context) : StoriesRoomDatabase {
         return INSTANCE ?: synchronized(this){
             INSTANCE ?: Room.databaseBuilder(
                 context.applicationContext,
                 StoriesRoomDatabase::class.java, "stories"
             )
                 .fallbackToDestructiveMigration()
                 .build()
                 .also { INSTANCE = it }
         }
     }
 }
}