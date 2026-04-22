package hu.bme.aut.genaidemo.ui.screen

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

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    private val genModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = "YOUR_KEY_HERE",
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.LOW_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.LOW_AND_ABOVE),
        )
    )

    fun generateContet(prompt: String) {
        viewModelScope.launch{
            try {

                val inputContent = content {
                    text(prompt)
                    //it can have images also..
                }

                /*var fullResponse = ""
                var result = genModel.generateContent(
                    inputContent)
                fullResponse = result.text!!

                // this adds the result to the flow and as it is collected as state
                // this will trigger a recomposition on the compose side
                _textGenerationResult.value = fullResponse*/

                var fullResponse = ""
                genModel.generateContentStream(inputContent).collect {
                        chunk ->
                    fullResponse += chunk.text
                    _textGenerationResult.value = fullResponse
                }


            } catch (e: Exception) {
                e.printStackTrace()
                _textGenerationResult.value = "Error: ${e.message}"
            }
        }
    }

}