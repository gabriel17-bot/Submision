package com.example.storyappfinal.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyappfinal.data.model.Login
import com.example.storyappfinal.data.model.Register
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
        val dummyEmail = "jamal123@gmail.com"
        val dummyPassword = "passwordnyaadadeh"

        Mockito.`when`(mockApiService.doLogin(dummyEmail, dummyPassword)).thenReturn(mockCallLogin)
        authViewModel.login(dummyEmail, dummyPassword, mockApiService)
        Mockito.verify(mockApiService).doLogin(dummyEmail, dummyPassword)
    }

    @Test
    fun register() {
        val dummyName = "Jamal"
        val dummyEmail = "ahmad@gmail.com"
        val dummyPassword = "ahmadjamal123"

        Mockito.`when`(mockApiService.doRegister(dummyEmail, dummyPassword, dummyName)).thenReturn(mockCallRegister)
        authViewModel.register(dummyEmail, dummyPassword, dummyName, mockApiService)
        Mockito.verify(mockApiService).doRegister(dummyEmail, dummyPassword, dummyName)
        Mockito.verify(mockApiService, Mockito.never()).doRegister(dummyName, dummyEmail, dummyPassword)
    }
}