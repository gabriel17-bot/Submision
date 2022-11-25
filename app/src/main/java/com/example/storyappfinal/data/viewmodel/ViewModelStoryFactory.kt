@file:Suppress("UNCHECKED_CAST")

package com.example.storyappfinal.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfinal.data.database.StoryDatabase
import com.example.storyappfinal.data.repository.remote.ApiService
import com.example.storyappfinal.data.repository.room.StoryRepository


class ViewModelStoryFactory(val context: Context, private val apiService: ApiService, val token:String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryPagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val database = StoryDatabase.getDatabase(context)
            return StoryPagerViewModel(
                StoryRepository(
                    database,
                    apiService, token
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}