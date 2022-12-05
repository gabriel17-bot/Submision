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
    suspend fun getUserPreferences() {
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

        val dummyPropertyUserName = Constanta.UserPreferences.UserName.name
        val dummyFlowUserName = flow { emit("UserName pengguna") }
        Mockito.`when`(pref.getUserName()).thenReturn(dummyFlowUserName)
        val actualResultUserName = settingViewModel.getUserPreferences(dummyPropertyUserName)
        val expectedResultUserName = dummyFlowUserName.asLiveData()
        Assert.assertEquals(expectedResultUserName.value, actualResultUserName.value)

        val dummyPropertyUserEmail = Constanta.UserPreferences.UserEmail.name
        val dummyFlowUserEmail = flow { emit("UserEmail pengguna") }
        Mockito.`when`(pref.getUserEmail()).thenReturn(dummyFlowUserEmail)
        val actualResultUserEmail = settingViewModel.getUserPreferences(dummyPropertyUserEmail)
        val expectedResultUserEmail = dummyFlowUserEmail.asLiveData()
        Assert.assertEquals(expectedResultUserEmail.value, actualResultUserEmail.value)

        val dummyPropertyUserLastLogin = Constanta.UserPreferences.UserLastLogin.name
        val dummyFlowUserLastLogin = flow { emit("UserLastLogin pengguna") }
        Mockito.`when`(pref.getUserLastLogin()).thenReturn(dummyFlowUserLastLogin)
        val actualResultUserLastLogin = settingViewModel.getUserPreferences(dummyPropertyUserLastLogin)
        val expectedResultUserLastLogin = dummyFlowUserLastLogin.asLiveData()
        Assert.assertEquals(expectedResultUserLastLogin.value, actualResultUserLastLogin.value)

    }

    @Test
    suspend fun setUserPreferences() {
        val dummyUserToken = "asdwaiopndnlnefoihwe"
        val dummyUserUid = "UserUid"
        val dummyUserName = "Username"
        val dummyUserEmail = "userEmail"
        Mockito.verify(pref.saveLoginSession(dummyUserToken,dummyUserUid,dummyUserName,dummyUserEmail))
    }

    @Test
    suspend fun clearUserPreferences() {
        Mockito.verify(pref.clearLoginSession())
    }
}