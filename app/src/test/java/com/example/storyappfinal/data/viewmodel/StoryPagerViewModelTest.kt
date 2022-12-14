package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.data.repository.StoryRepository
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

    private lateinit var storyPagerViewModel: StoryPagerViewModel
    @Mock
    private lateinit var storyRepository: StoryRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun doBefore() {
        storyPagerViewModel = StoryPagerViewModel(storyRepository)
    }

    @Test
    fun getStory() {
        //Testing Get Story Should Not Null and Return Success
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
        Mockito.verify(storyRepository).getStory()
        Assert.assertEquals(expectedStory.value, actualStory.value)
        Assert.assertNotNull(actualStory.value)
    }
}