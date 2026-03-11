package hu.ait.highlowgame.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HelpScreen(userName: String, num: Int) {
    Column() {
        Text("$userName $num: Press the start button to start the game, lorem ipsum...")
    }
}