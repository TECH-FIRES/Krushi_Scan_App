package com.example.krushiscan
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

object ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/") // Use 10.0.2.2 for Android Emulator to access localhost
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun uploadImage(filePath: String, callback: (String) -> Unit) {
        val file = File(filePath)
        if (!file.exists()) {
            callback("File not found")
            return
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = apiService.uploadImage(body)
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: retrofit2.Response<String>) {
                if (response.isSuccessful) {
                    callback(response.body() ?: "Success")
                } else {
                    callback("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                callback("Failure: ${t.message}")
            }
        })
    }
}

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part file: MultipartBody.Part): retrofit2.Call<String>
}
