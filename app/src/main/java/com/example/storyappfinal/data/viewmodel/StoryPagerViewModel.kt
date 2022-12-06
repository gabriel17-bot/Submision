package com.example.storyappfinal.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.StoryRepository

class StoryPagerViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun story(): LiveData<PagingData<Story>> {
        return storyRepository.getStory()
    }
}
