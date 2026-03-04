package hu.ait.highlowgame.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen() {
    var randomNr by remember { mutableStateOf(0)}
    var guess by remember { mutableStateOf("")}
    var resultText by remember { mutableStateOf("-")}

    var isInputError by remember { mutableStateOf(false)}

    LaunchedEffect(key1 = Unit) {
        randomNr = (0..3).random()
    }

    fun validateInput(input: String) {
        try {
            val myNum = input.toInt()
            isInputError = false
        } catch (e: Exception) {
            isInputError = true
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            isError = isInputError,
            value = "$guess",
            label = { Text("Enter your guess here:") },
            onValueChange = {
                // it is the text what was typed currently in the textbox
                guess = it
                validateInput(guess)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )


        Button(onClick = {
            try {
                isInputError = false
                val userGuessNr = guess.toInt()
                if (userGuessNr == randomNr) {
                    // win the game
                    resultText = "Well done, you have won the game!"
                } else if (userGuessNr > randomNr) {
                    resultText = "The number is lower"
                } else if (userGuessNr < randomNr) {
                    resultText = "The number is higher"
                }

            } catch (e: Exception) {
                isInputError = true
                e.printStackTrace()
            }
        }) {
            Text("Guess")
        }

        Text("$resultText",
            fontSize = 30.sp)
    }
}