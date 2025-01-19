package com.example.taskflow.User

import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.68.114:8080/")
        .addConverterFactory(GsonConverterFactory.create()).build()


    val apiService = retrofit.create(UserInterface::class.java)


    fun registerUser(user: UserRegister) {
        apiService.register(user).enqueue(object : Callback<UserRegister> {
            override fun onResponse(call: Call<UserRegister>, response: Response<UserRegister>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    println("Rejestracja zakończona sukcesem: ${responseBody}")
                } else {
                    println("Błąd rejestracji: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserRegister>, t: Throwable) {
                println("Błąd połączenia: ${t.message}")
            }
        })
    }
}