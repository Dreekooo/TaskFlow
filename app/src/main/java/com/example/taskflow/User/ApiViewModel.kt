package com.example.taskflow.User

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskflow.addProject.users.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {

    companion object {
        private const val BASE_URL = "http://192.168.68.116:8080/"
        private const val TAG = "ApiViewModel"
    }

    var token by mutableStateOf<String?>(null)

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(UserInterface::class.java)

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
                    Log.d(TAG, "Token: $token")
                    token?.let { getCurrentUser(it) }
                } else {
                    Log.e(TAG, "Błąd logowania: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Błąd połączenia: ${t.message}")
            }
        })
    }

    private fun getCurrentUser(token: String) {
        apiService.getCurrentUser("Bearer $token").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d(TAG, "Zalogowany użytkownik ID: ${user?.id}")
                } else {
                    Log.e(
                        TAG,
                        "Błąd pobierania danych użytkownika: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "Błąd połączenia: ${t.message}")
            }
        })
    }
}