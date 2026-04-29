package hu.bme.ait.generativeaidemo.ui.screen.genaitext

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GenAIScreen(
    modifier: Modifier = Modifier,
    viewModel: GenAIViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var textPrompt by rememberSaveable { mutableStateOf("Tell me the current time") }
    val textResult = viewModel.textGenerationResult.collectAsState().value

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        horizontalAlignment = CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textPrompt,
            onValueChange = {
                textPrompt = it
            }
        )
        Button(onClick = {
            viewModel.generateContent(textPrompt)
        }) {
            Text(text = "Generate")
        }
        Text(text = textResult ?: "No content generated yet")
    }


}