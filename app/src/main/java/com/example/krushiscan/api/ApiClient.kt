package com.example.krushiscan.api

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.Timeout
import java.io.File
import java.io.IOException

class ApiClient {

    private val client = OkHttpClient()

    fun uploadImage(filePath: String, callback: Callback) {
        val file = File(filePath)
        if (!file.exists()) {
            callback.onFailure(
                CallDummy(),
                IOException("File does not exist at path: $filePath")
            )
            return
        }

        val mediaType = "image/*".toMediaTypeOrNull()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                file.asRequestBody(mediaType)
            )
            .build()

        val request = Request.Builder()
            .url("http://127.0.0.1:8000/upload")  // Replace with your actual FastAPI endpoint
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(callback)
    }
}

// Dummy Call implementation to handle failure when file not found
class CallDummy : Call {
    override fun enqueue(responseCallback: Callback) {}
    override fun execute(): Response = throw IOException("Dummy call")
    override fun cancel() {}
    override fun isExecuted(): Boolean = false
    override fun isCanceled(): Boolean = false
    override fun request(): Request = Request.Builder().url("http://localhost").build()
    override fun clone(): Call = this
    override fun timeout(): Timeout = Timeout.NONE
}
