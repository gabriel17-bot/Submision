package com.example.storyappfinal.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyappfinal.data.database.StoryDatabase
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.remote.ApiService
import com.example.storyappfinal.data.repository.remote.StoryRemoteMediator

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val token:String
) {
    fun getStory(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
            pagingSourceFactory = { storyDatabase.storyDao().getAllStory() }
        ).liveData
    }
}