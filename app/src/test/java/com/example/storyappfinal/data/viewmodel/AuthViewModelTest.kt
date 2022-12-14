package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyappfinal.data.model.Login
import com.example.storyappfinal.data.model.Register
import com.example.storyappfinal.data.model.User
import com.example.storyappfinal.data.repository.remote.ApiService
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @Mock
    private lateinit var mockCallLogin: Call<Login>
    @Mock
    private lateinit var mockCallRegister: Call<Register>
    @Mock
    private lateinit var mockApiService: ApiService
    private lateinit var authViewModel: AuthViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun doBefore() {
        authViewModel = AuthViewModel()
    }

    @Test
    fun login() {
        //Testing login Should Not Null and Success
        val dummyEmail = "jamal123@gmail.com"
        val dummyPassword = "passwordnyaadadeh"
        val dummyLogin = Login(User("Nama User",dummyEmail,dummyPassword), false, "empty message")

        Mockito.`when`(mockApiService.doLogin(dummyEmail, dummyPassword)).thenReturn(mockCallLogin)
        Mockito.`when`(mockCallLogin.enqueue(any())).then{
            authViewModel.loginResult.postValue(dummyLogin)
        }
        authViewModel.login(dummyEmail, dummyPassword, mockApiService)
        println("ExpectedRegister: ${authViewModel.loginResult.value}")
        Mockito.verify(mockApiService).doLogin(dummyEmail, dummyPassword)
        Assert.assertNotNull(authViewModel.loginResult.value)
        Assert.assertEquals(dummyLogin, authViewModel.loginResult.value)
    }

    @Test
    fun register() {
        //Testing register Should Not Null and Success
        val dummyName = "Jamal"
        val dummyEmail = "ahmad@gmail.com"
        val dummyPassword = "ahmadjamal123"
        val dummyRegister = Register(false,"empty message")

        Mockito.`when`(mockApiService.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(mockCallRegister)
        Mockito.`when`(mockCallRegister.enqueue(any())).then{
            authViewModel.registerResult.postValue(dummyRegister)
        }
        authViewModel.register(dummyEmail, dummyPassword, dummyName, mockApiService)
        println("ExpectedRegister: ${authViewModel.registerResult.value}")
        Mockito.verify(mockApiService).doRegister(dummyEmail, dummyPassword, dummyName)
        Assert.assertNotNull(authViewModel.registerResult.value)
        Assert.assertEquals(dummyRegister, authViewModel.registerResult.value)
    }
}