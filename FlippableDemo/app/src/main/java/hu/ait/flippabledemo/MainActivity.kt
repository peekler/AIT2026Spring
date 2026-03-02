package hu.ait.flippabledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import hu.ait.flippabledemo.ui.theme.FlippableDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlippableDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val mainFlipController = rememberFlipController()

    Column(modifier = modifier) {

        Flippable(
            frontSide = { CardFront() },
            backSide = { CardBack() },
            flipController = mainFlipController,
            flipOnTouch = true
        )


        Button(onClick = {
            mainFlipController.flip()
        }) {
            Text("Flip")
        }

    }
}


@Composable
fun CardFront() {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF0077B6), Color(0xFF00B4D8))
    )

    Card(
        modifier = Modifier
            .size(400.dp, 250.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("MyBank", fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
                Column {
                    Text("1234 5678 9012 3456", fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text("VALID THRU", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("12/27", fontSize = 12.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("PETER EKLER", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CardBack() {
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF0077B6), Color(0xFF00B4D8))
    )
    Card(
        modifier = Modifier
            .size(400.dp, 250.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.background(gradient).fillMaxSize()){
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Box (modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Black))
                Spacer(modifier = Modifier.height(24.dp))
                Text("Authorised signature",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.White)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 8.dp)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)) {
                    Text("CVC: ", color = Color.White)
                    Text("123", color = Color.Black, modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlippableDemoTheme {
        Greeting("Android")
    }
}