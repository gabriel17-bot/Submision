package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {

    @Mock
    private lateinit var pref: SettingPreferences
    private lateinit var settingViewModel: SettingViewModel
    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun doBefore() {
        settingViewModel = SettingViewModel(pref)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun doAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun getUserPreferences() {
        val dummyPropertyUserID = Constanta.UserPreferences.UserUID.name
        val dummyFlowUserId = flow { emit("UserId pengguna") }
        Mockito.`when`(pref.getUserUid()).thenReturn(dummyFlowUserId)
        val actualResultUserID = settingViewModel.getUserPreferences(dummyPropertyUserID)
        val expectedResultUserID = dummyFlowUserId.asLiveData()
        Assert.assertEquals(actualResultUserID.value, expectedResultUserID.value)

        val dummyPropertyUserToken = Constanta.UserPreferences.UserToken.name
        val dummyFlowUserToken = flow { emit("UserToken pengguna") }
        Mockito.`when`(pref.getUserToken()).thenReturn(dummyFlowUserToken)
        val actualResultUserToken = settingViewModel.getUserPreferences(dummyPropertyUserToken)
        val expectedResultUserToken = dummyFlowUserToken.asLiveData()
        Assert.assertEquals(expectedResultUserToken.value, actualResultUserToken.value)
    }

    @Test
    fun setUserPreferences() {
    }

    @Test
    fun clearUserPreferences() {
    }
}