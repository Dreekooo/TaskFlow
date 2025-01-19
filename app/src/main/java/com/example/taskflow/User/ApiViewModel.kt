package com.example.taskflow.User

import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.114:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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

    fun loginUser(user: UserLogin) {
        apiService.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        println("Zalogowano pomyślnie. Token: $token")
                    } else {
                        println("Brak tokena w odpowiedzi.")
                    }
                } else {
                    println("Błąd logowania: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("Błąd połączenia: ${t.message}")
            }
        })
    }

}
