package com.example.taskflow.User

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {

    var token by mutableStateOf<String?>(null)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.116:8080/")
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
                    token = response.body()?.token
                    if (token != null) {
                        Log.d("TOken: ", "$token")
                        getUserData(token!!)
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

    fun getUserData(token: String) {
        val apiService = retrofit.create(UserInterface::class.java)

        apiService.getUserData(token).enqueue(object : Callback<UserLogin> {
            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    println("Dane użytkownika: $userData")
                } else {
                    println("Błąd: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                println("Błąd połączenia: ${t.message}")
            }
        })
    }

    val apiService1 =
        retrofit.create(com.example.taskflow.addProject.users.UserInterface::class.java)


    suspend fun getUserIdByUsername(username: String): Int? {
        val users = apiService1.getAllUsers()
        val user = users.find { it.username == username }
        return user?.id
    }


}
