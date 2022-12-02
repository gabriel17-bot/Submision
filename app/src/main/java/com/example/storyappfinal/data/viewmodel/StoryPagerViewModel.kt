package com.example.storyappfinal.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.repository.StoryRepository

class StoryPagerViewModel(storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<Story>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}
