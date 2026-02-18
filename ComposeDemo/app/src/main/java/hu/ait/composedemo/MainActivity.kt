package hu.ait.composedemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import hu.ait.composedemo.ui.theme.ComposeDemoTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()



        setContent {
            ComposeDemoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()

                ) { innerPadding ->
                    /*Greeting(
                        name = "AIT BEST MOBILE TEAM",
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    )*/
                    MyColumn(
                        modifier = Modifier.padding(
                            innerPadding
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MyColumn(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text("Hello")
        Text("AIT")
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var currentDataAndTime by rememberSaveable {
        mutableStateOf("")
    }

    var inputText by rememberSaveable {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        Text(
            text = "Hello $name!"
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            onValueChange = {
                input->
                inputText = input
            }
        )

        Text(
            text = "Date and time: $currentDataAndTime",
            fontSize = 28.sp
        )

        Button(
            onClick = {
                currentDataAndTime = Date(
                    System.currentTimeMillis()
                ).toString()
            },
        ) {
            Text("Show time")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting("Budapest")
    }
}