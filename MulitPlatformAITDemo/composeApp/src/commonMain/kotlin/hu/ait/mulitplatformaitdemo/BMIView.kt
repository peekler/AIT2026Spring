package hu.ait.mulitplatformaitdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BMIView() {
    var height by remember { mutableStateOf(0.0) }
    var weight by remember { mutableStateOf(0.0) }
    var BMIIndex by remember { mutableStateOf(0.0) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Height in m (e.g. 1.8):")
        TextField(
            value = height.toString(),
            onValueChange = { height = it.toDoubleOrNull() ?: 0.0 },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Text("Weight in kg:")
        TextField(
            value = weight.toString(),
            onValueChange = { weight = it.toDoubleOrNull() ?: 0.0 },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {
                if (height <= 0 || weight <= 0) {
                    showError = true
                } else {
                    showError = false
                    BMIIndex = weight / (height * height)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Show BMI")
        }

        if (showError) {
            Text("Error in the inputs")
        } else {
            Text("BMI index: $BMIIndex")
            Text(
                """
                <18.5: underweight
                18.5 – 24.9: normal
                25<: overweight
                """
            )
        }
    }
}