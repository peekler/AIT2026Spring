package hu.ait.highlowgame.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(onStartClicked: ()->Unit,
               onHelpClicked: ()->Unit,
               onAboutClicked: ()->Unit
) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Button(onClick = {
            onStartClicked()
        }) {
            Text("Start")
        }
        Button(onClick = {onHelpClicked()}) {
            Text("Help")
        }
        Button(onClick = {onAboutClicked()}) {
            Text("About")
        }
    }
}