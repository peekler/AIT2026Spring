package hu.ait.highlowgame.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {

    var guess by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("-") }
    var isInputError by remember { mutableStateOf(false) }
    var showWinDialog by remember { mutableStateOf(false) }

    fun validateInput(input: String) {
        try {
            val myNum = input.toInt()
            isInputError = false
        } catch (e: Exception) {
            isInputError = true
            if (guess.isNotEmpty())
                guess = guess.substring(0, guess.length - 1)
        }
    }

    Column(
        modifier = Modifier
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
            ),
            trailingIcon = {
                if (isInputError)
                    Icon(
                        Icons.Filled.Warning,
                        "error",
                        tint = MaterialTheme.colorScheme.error
                    )
            },
            leadingIcon = {
                Icon(Icons.Filled.Menu, "data")
            }
        )

        if (isInputError) {
            Text(
                "Invalid input",
                color = Color.Red,
                fontSize = 16.sp
            )
        }


        Button(
            enabled = !isInputError,
            onClick = {
            try {
                isInputError = false
                val userGuessNr = guess.toInt()
                if (userGuessNr == viewModel.generatedNumber.value) {
                    // win the game
                    resultText = "Well done, you have won the game!"
                    showWinDialog = true
                } else if (userGuessNr > viewModel.generatedNumber.value) {
                    resultText = "The number is lower"
                } else if (userGuessNr < viewModel.generatedNumber.value) {
                    resultText = "The number is higher"
                }

            } catch (e: Exception) {
                isInputError = true
                e.printStackTrace()
            }
        }) {
            Text("Guess")
        }

        Text(
            "$resultText",
            fontSize = 30.sp
        )
    }

    if (showWinDialog) {
        WinDialog(
            onDialogOk = {
                showWinDialog = false
                viewModel.reset()
            },
            onDialogCancel = { showWinDialog = false }
        )
    }
}

@Composable
fun WinDialog(
    onDialogOk: () -> Unit,
    onDialogCancel: () -> Unit
) {
    AlertDialog(
        title = { Text("Well done!") },
        text = { Text("You have won!") },
        confirmButton = {
            TextButton(onClick = {
                onDialogOk()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDialogCancel()
            }) {
                Text("Cancel")
            }
        },
        // this is called when <- arrow is pressed or user clicks behind the dialog
        onDismissRequest = {
            onDialogCancel()
        },
        //shape = CircleShape

    )
}