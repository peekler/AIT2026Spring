package hu.ait.sideeffectdemo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen() {

    LaunchedEffect(key1 = Unit) {
        Log.d("TAG_LIFE", "MainScreen added")
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            Log.d("TAG_LIFE", "MainScreen removed")
        }
    }


    Column(modifier = Modifier.fillMaxSize().background(Color.Green)) {
        Text("MAIN SCREEN")
    }
}