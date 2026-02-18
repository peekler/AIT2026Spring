package hu.ait.tictactoedemo


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import hu.ait.tictactoedemo.ui.theme.TicTacToeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameArea(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GameArea(modifier: Modifier = Modifier) {
    var circleX by remember { mutableStateOf(0f) }
    var circleY by remember { mutableStateOf(0f) }
    var points by remember { mutableStateOf<List<Offset>>(
        emptyList()) }


    Column(
        modifier = modifier
    ) {
        Text("Welcome in TicTacToe",
            fontSize = 30.sp)

        Button(onClick = {
            points = emptyList()
        }) {
            Text("Reset")
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
                .pointerInput(key1 = Unit) {
                    detectTapGestures {
                        offset ->
                         Log.d("TAG_CANVAS", "${offset.x}," +
                                 " ${offset.x}")

                        //circleX = offset.x
                        //circleY = offset.y
                        // append the new coordinate to the list
                        points = points + offset
                    }
                }
        ) {
            drawLine(
                color = Color.Red,
                start = Offset(0f, 0f),
                end = Offset(size.width.toFloat(),
                    size.height.toFloat()),
                strokeWidth = 7f
            )


            points.forEach {
                drawCircle(
                    color = Color.Blue,
                    style = Stroke(width = 5f),
                    center = Offset(it.x,
                        it.y),
                    radius = 50f
                )
            }




        }

    }
}
