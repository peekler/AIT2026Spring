package hu.ait.sideeffectdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hu.ait.sideeffectdemo.ui.theme.SideEffectDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SideEffectDemoTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    // 0: main screen, 1: details screen
    var screenNr by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(key1 = screenNr) {
        Log.d("TAG_LIFE", "screenNr state changed")
    }

    SideEffect {
        Log.d("TAG_LIFE", "some state has changed")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = screenNr == 0,
                    onClick = { screenNr = 0 },
                    label = { Text("Main") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Main") }
                )
                NavigationBarItem(
                    selected = screenNr == 1,
                    onClick = { screenNr = 1 },
                    label = { Text("Details") },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Details") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (screenNr == 0) {
                MainScreen()
            } else {
                DetailsScreen()
            }
        }
    }
}
