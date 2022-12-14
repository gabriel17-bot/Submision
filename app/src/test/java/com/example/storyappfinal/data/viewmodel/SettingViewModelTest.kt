package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
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
    fun getUserPreferences() = runTest{
        val dummyId = "Ini id paslu"
        val dummyPropertyUserID = Constanta.UserPreferences.UserUID.name
        val dummyFlowUserId = flowOf(dummyId)
        Mockito.`when`(pref.getUserUid()).thenReturn(dummyFlowUserId)

        val actualLiveData = settingViewModel.getUserPreferences(dummyPropertyUserID)
        Mockito.verify(pref).getUserUid()
        var actualResult = ""
        actualLiveData.observeForever {
            actualResult = it
        }

        delay(100)
        println("Expected: $dummyId")
        println("ActualId: $actualResult")
        Assert.assertEquals(dummyId, actualResult)
    }

    @Test
    fun setUserPreferences() = runTest {
        val dummyUserToken = "asdwaiopndnlnefoihwe"
        val dummyUserUid = "UserUid"
        val dummyUserName = "Username"
        val dummyUserEmail = "userEmail"
        settingViewModel.setUserPreferences(dummyUserToken,dummyUserUid,dummyUserName,dummyUserEmail)
        advanceUntilIdle()
        Mockito.verify(pref).saveLoginSession(dummyUserToken,dummyUserUid,dummyUserName,dummyUserEmail)
    }

    @Test
    fun clearUserPreferences() = runTest {
        settingViewModel.clearUserPreferences()
        advanceUntilIdle()
        Mockito.verify(pref).clearLoginSession()
    }
}