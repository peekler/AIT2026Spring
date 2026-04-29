package hu.bme.ait.generativeaidemo.ui.screen.genaivision

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content


class GenAIVisionViewModel : ViewModel() {

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = "YOUR_KEY_HERE"
    )

    fun sendPrompt(
        bitmap: Bitmap,
        prompt: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }

                var fullResponse = ""
                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    fullResponse += chunk.text
                    _textGenerationResult.value = fullResponse
                }
            } catch (e: Exception) {
                _textGenerationResult.value = "Error ${System.currentTimeMillis()}: ${e.message}"
            }
        }
    }
}

