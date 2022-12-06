package com.example.storyappfinal.data.viewmodel

import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.res.ResourcesCompat
import com.example.storyappfinal.data.model.StoryList
import com.example.storyappfinal.data.model.StoryUpload
import com.example.storyappfinal.data.repository.remote.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import java.io.File

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
        Assert.assertNotNull(storyViewModel.loadStoryLocationData(context,dummyToken,mockApiService))
    }

    @Test
    fun uploadNewStory() {
        val dummyToken = "asldjnfoeubasmdlawkdnqweasdbrgs"
        val dummyDescription = "Ini adalah deskripsi dari story yang di upload"
        val requestDescription = dummyDescription.toRequestBody("text/plain".toMediaType())
        val mockImageFile = mock(File::class.java)
        val requestImageFile = mockImageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val fakeImageMultipart = MultipartBody.Part.createFormData(
            "photo",
            mockImageFile.name,
            requestImageFile
        )
        Mockito.`when`(mockApiService.doUploadImage(any(dummyToken::class.java) , any(fakeImageMultipart::class.java), any(requestDescription::class.java))).thenReturn(mockCallUpload)
        storyViewModel.uploadNewStory(context,dummyToken, mockImageFile, dummyDescription, mockApiService)
        Mockito.verify(mockApiService).doUploadImage(any(dummyToken::class.java) , any(fakeImageMultipart::class.java), any(requestDescription::class.java))
    }

     private fun <T> any(types: Class<T>): T = Mockito.any(types)
}