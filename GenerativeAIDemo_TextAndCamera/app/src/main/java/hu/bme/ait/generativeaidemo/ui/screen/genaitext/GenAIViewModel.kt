package hu.bme.ait.generativeaidemo.ui.screen.genaitext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenAIViewModel : ViewModel() {

    private val genModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = "YOUR_KEY_HERE",
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.LOW_AND_ABOVE)
        )
    )

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    fun generateContent(prompt: String = "Tell me the current time") {
        _textGenerationResult.value = "Generating..."
        viewModelScope.launch {
            try {
                val inputContent = content {
                    text(prompt)
                }

                var fullResponse = ""
                genModel.generateContentStream(inputContent).collect { chunk ->
                    fullResponse += chunk.text
                    _textGenerationResult.value = fullResponse
                }

            } catch (e: Exception) {
                _textGenerationResult.value = "Error: ${e.message}"
            }
        }
    }

}