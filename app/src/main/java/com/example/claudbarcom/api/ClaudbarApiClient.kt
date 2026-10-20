package com.example.claudbarcom.api

import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ClaudbarApiClient(
    private val baseUrl: String = "https://api.claudbar.com",
    private val token: String? = null
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // ========== AUTENTICACIÓN ==========
    suspend fun login(email: String, password: String): LoginResponse {
        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        return post<LoginResponse>("/auth/login", json)
    }

    suspend fun register(email: String, password: String): LoginResponse {
        val json = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        return post<LoginResponse>("/auth/register", json)
    }

    suspend fun getMe(): UserResponse {
        return get<UserResponse>("/me")
    }

    // ========== CHAT / IA ==========
    suspend fun chat(messages: List<Message>, model: String = "flash", maxTokens: Int = 2048): ChatResponse {
        val json = JSONObject().apply {
            put("messages", org.json.JSONArray(messages.map { it.toJSON() }))
            put("model", model)
            put("max_tokens", maxTokens)
        }
        return post<ChatResponse>("/v1/chat/completions", json)
    }

    suspend fun agent(messages: List<Message>, maxTokens: Int = 2048): ChatResponse {
        val json = JSONObject().apply {
            put("messages", org.json.JSONArray(messages.map { it.toJSON() }))
            put("max_tokens", maxTokens)
        }
        return post<ChatResponse>("/v1/agent", json)
    }

    // ========== TTS / VOZ ==========
    suspend fun generateTts(text: String, voice: String = "isabela"): ByteArray {
        val json = JSONObject().apply {
            put("text", text)
            put("voice", voice)
        }
        return postBinary("/v1/tts", json)
    }

    // ========== BÚSQUEDA ==========
    suspend fun search(query: String): SearchResponse {
        val json = JSONObject().apply {
            put("query", query)
        }
        return post<SearchResponse>("/v1/search", json)
    }

    // ========== HERRAMIENTAS INTERNAS ==========
    private suspend inline fun <reified T> get(endpoint: String): T {
        val request = okhttp3.Request.Builder()
            .url("$baseUrl$endpoint")
            .header("Authorization", "Bearer ${token ?: ""}")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: "{}"

        if (!response.isSuccessful) {
            throw ApiException(response.code, body)
        }

        return parseJson<T>(body)
    }

    private suspend inline fun <reified T> post(endpoint: String, json: JSONObject): T {
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = okhttp3.Request.Builder()
            .url("$baseUrl$endpoint")
            .post(body)
            .header("Authorization", "Bearer ${token ?: ""}")
            .header("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: "{}"

        if (!response.isSuccessful) {
            throw ApiException(response.code, responseBody)
        }

        return parseJson<T>(responseBody)
    }

    private suspend fun postBinary(endpoint: String, json: JSONObject): ByteArray {
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = okhttp3.Request.Builder()
            .url("$baseUrl$endpoint")
            .post(body)
            .header("Authorization", "Bearer ${token ?: ""}")
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw ApiException(response.code, "Binary response error")
        }

        return response.body?.bytes() ?: ByteArray(0)
    }

    private inline fun <reified T> parseJson(json: String): T {
        return when (T::class) {
            LoginResponse::class -> JSONObject(json).let { obj ->
                LoginResponse(
                    token = obj.getString("token"),
                    user = User(
                        id = obj.getJSONObject("user").getString("id"),
                        email = obj.getJSONObject("user").getString("email"),
                        plan = obj.getJSONObject("user").optString("plan", "free")
                    )
                )
            } as T
            UserResponse::class -> JSONObject(json).let { obj ->
                UserResponse(
                    id = obj.getString("id"),
                    email = obj.getString("email"),
                    plan = obj.optString("plan", "free")
                )
            } as T
            ChatResponse::class -> JSONObject(json).let { obj ->
                val choice = obj.getJSONArray("choices").getJSONObject(0)
                ChatResponse(
                    content = choice.getJSONObject("message").getString("content"),
                    model = obj.optString("model", "")
                )
            } as T
            SearchResponse::class -> JSONObject(json).let { obj ->
                val results = mutableListOf<SearchResult>()
                obj.getJSONArray("results").let { arr ->
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        results.add(SearchResult(
                            title = item.getString("title"),
                            url = item.getString("url"),
                            snippet = item.getString("snippet")
                        ))
                    }
                }
                SearchResponse(results)
            } as T
            else -> throw IllegalArgumentException("Unknown type: ${T::class.simpleName}")
        }
    }
}

// ========== MODELOS DE DATOS ==========
data class LoginResponse(val token: String, val user: User)
data class UserResponse(val id: String, val email: String, val plan: String)
data class ChatResponse(val content: String, val model: String)
data class SearchResponse(val results: List<SearchResult>)
data class SearchResult(val title: String, val url: String, val snippet: String)
data class User(val id: String, val email: String, val plan: String)

data class Message(
    val role: String,
    val content: String
) {
    fun toJSON() = JSONObject().apply {
        put("role", role)
        put("content", content)
    }
}

class ApiException(val code: Int, val message: String) : Exception("API Error ($code): $message")
