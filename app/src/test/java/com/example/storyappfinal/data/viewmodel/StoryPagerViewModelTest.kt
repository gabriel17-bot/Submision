package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.repository.StoryRepository
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryPagerViewModelTest {

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyPagerViewModel: StoryPagerViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun doBefore() {
        storyPagerViewModel = StoryPagerViewModel(storyRepository)
    }

    @Test
    fun getStory() {
        val expectedStory = MutableLiveData<PagingData<Story>>()
        val fakePagingData = PagingData.from(listOf(
            Story(
                "12345678",
                "japan.jpg",
                "2 Desember 2022",
                "Jamal",
                "Foto di Jepang yang sangat bagus"
            )
        ))
        expectedStory.value = fakePagingData
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStory)
        val actualStory = storyPagerViewModel.story()
        println(actualStory.value)
        Mockito.verify(storyRepository).getStory()
        Assert.assertEquals(expectedStory.value, actualStory.value)
    }
}