package hu.bme.aut.genaidemo.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GenAIScreen(
    modifier: Modifier = Modifier,
    viewModel: GenAIViewModel = viewModel()
) {

    var textPrompt by rememberSaveable {
        mutableStateOf("Write me a poem about dogs and cats, it should be minium 10 lines") }
    val textResult = viewModel.textGenerationResult.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textPrompt,
            onValueChange = {
                textPrompt = it
            }
        )

        Button(onClick = {
            viewModel.generateContet(textPrompt)
        }) {
            Text("Generate")
        }

        Text(text = textResult.value ?: "No content generated",
            fontSize = 28.sp)
    }

}