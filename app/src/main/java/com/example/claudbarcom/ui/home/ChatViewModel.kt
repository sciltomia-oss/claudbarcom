package com.example.claudbarcom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.claudbarcom.api.ClaudbarApiClient
import com.example.claudbarcom.api.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val api: ClaudbarApiClient = ClaudbarApiClient()) : ViewModel() {

    // Estado del chat
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _response = MutableStateFlow<String>("")
    val response: StateFlow<String> = _response

    // Estado de la grabación de voz
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _audioData = MutableStateFlow<ByteArray?>(null)
    val audioData: StateFlow<ByteArray?> = _audioData

    // ========== CHAT ==========
    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                _isLoading.value = true

                // Agregar mensaje del usuario
                val userMessage = Message("user", text)
                _messages.value = _messages.value + userMessage

                // Enviar a IA
                val response = api.chat(
                    messages = _messages.value,
                    model = "flash",
                    maxTokens = 1024
                )

                // Agregar respuesta
                val aiMessage = Message("assistant", response.content)
                _messages.value = _messages.value + aiMessage
                _response.value = response.content

            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ========== BAR LIVE (VOZ) ==========
    fun startRecording() {
        _isRecording.value = true
    }

    fun stopRecording() {
        _isRecording.value = false
    }

    fun sendAudio(audioBytes: ByteArray) {
        viewModelScope.launch {
            try {
                _error.value = null
                _isLoading.value = true
                _audioData.value = audioBytes

                // Aquí irá: convertir audio a texto (STT)
                // Luego: enviar texto a IA
                // Finalmente: convertir respuesta a audio (TTS)

                val placeholderText = "Mensaje de voz recibido"
                sendMessage(placeholderText)

            } catch (e: Exception) {
                _error.value = e.message ?: "Error al procesar audio"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun playResponse(text: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val audioBytes = api.generateTts(text, "isabela")
                // Aquí se reproducirá el audio
                _audioData.value = audioBytes
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al generar voz"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ========== BÚSQUEDA ==========
    fun search(query: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                _isLoading.value = true
                val results = api.search(query)
                // Procesar resultados de búsqueda
            } catch (e: Exception) {
                _error.value = e.message ?: "Error en búsqueda"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _messages.value = emptyList()
        _response.value = ""
        _error.value = null
    }
}
