package com.example.storyappfinal.data.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappfinal.data.model.Login
import com.example.storyappfinal.data.model.Register
import com.example.storyappfinal.data.repository.remote.ApiService
import com.example.storyappfinal.utils.Constanta
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    var loading = MutableLiveData(View.GONE)
    val error = MutableLiveData("")
    val tempEmail = MutableLiveData("")
    val loginResult = MutableLiveData<Login>()
    val registerResult = MutableLiveData<Register>()

    fun login(email: String, password: String, apiService: ApiService) {
        tempEmail.postValue(email)
        loading.postValue(View.VISIBLE)
        val client = apiService.doLogin(email, password)
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    loginResult.postValue(response.body())
                } else {
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        error.postValue("LOGIN ERROR : $errorMessages")
                    }
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(Constanta.TAG_AUTH, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }

    fun register(name: String, email: String, password: String, apiService: ApiService) {
        loading.postValue(View.VISIBLE)
        val client = apiService.doRegister(name, email, password)
        client.enqueue(object : Callback<Register> {
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                if (response.isSuccessful) {
                    registerResult.postValue(response.body())
                } else {
                    response.errorBody()?.let {
                        val errorResponse = JSONObject(it.string())
                        val errorMessages = errorResponse.getString("message")
                        error.postValue("REGISTER ERROR : $errorMessages")
                    }
                }
                loading.postValue(View.GONE)
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                loading.postValue(View.GONE)
                Log.e(Constanta.TAG_AUTH, "onFailure Call: ${t.message}")
                error.postValue(t.message)
            }
        })
    }
}