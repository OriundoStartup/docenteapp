package com.example.docenteapp.data.remote

import com.example.docenteapp.domain.models.User
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiService(
    private val client: HttpClient,
    private val baseUrl: String = "http://localhost:8080"
) {
    suspend fun login(email: String, password: String): User {
        val targetUrl = if (baseUrl.contains("localhost")) baseUrl.replace("localhost", "10.0.2.2") else baseUrl
        return client.post("$targetUrl/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email, "password" to password))
        }.body()
    }
}
