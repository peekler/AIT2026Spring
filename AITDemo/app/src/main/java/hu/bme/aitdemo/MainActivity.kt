package hu.bme.aitdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aitdemo.ui.theme.AITDemoTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AITDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "AIT",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf("Data") }
    var input by remember { mutableStateOf("") }

    Column(modifier = modifier) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = input,
            onValueChange = {
                input = it
            }
        )

        Button(onClick = {
            //result = Date(System.currentTimeMillis()).toString()
            result = "Length: ${input.length}"
        }
        ) {
            Text("Show")
        }

        Text(
            text = "Result: $result",
            fontSize = 30.sp
        )
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AITDemoTheme {
        Greeting("Android")
    }
}