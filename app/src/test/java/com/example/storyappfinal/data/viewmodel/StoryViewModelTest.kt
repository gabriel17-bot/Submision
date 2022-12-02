package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyappfinal.data.model.StoryList
import com.example.storyappfinal.data.model.StoryUpload
import com.example.storyappfinal.data.repository.remote.ApiService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @Mock
    private lateinit var mockCallLoadStory: Call<StoryList>
    @Mock
    private lateinit var mockCallUpload: Call<StoryUpload>
    @Mock
    private lateinit var mockApiService: ApiService
    @Mock
    private lateinit var context: android.content.Context
    private lateinit var storyViewModel: StoryViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun doBefore() {
        storyViewModel = StoryViewModel()
    }

    @Test
    fun loadStoryLocationData() {
        val dummyToken = "asldjnfoeubasmdlawkdn"

        Mockito.`when`(mockApiService.getStoryListLocation(dummyToken,100)).thenReturn(mockCallLoadStory)
        storyViewModel.loadStoryLocationData(context,dummyToken,mockApiService)
        Mockito.verify(mockApiService).getStoryListLocation(dummyToken, 100)
    }

    @Test
    fun uploadNewStory() {
        val dummyToken = "asldjnfoeubasmdlawkdn"
        val dummyDescription = "Ini adalah deskripsi dari story yang di upload"
    }
}