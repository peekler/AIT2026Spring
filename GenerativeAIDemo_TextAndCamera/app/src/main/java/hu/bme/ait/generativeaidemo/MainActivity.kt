package hu.bme.ait.generativeaidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import hu.bme.ait.generativeaidemo.ui.navigation.CurrentScreen
import hu.bme.ait.generativeaidemo.ui.navigation.GenAITextRoute
import hu.bme.ait.generativeaidemo.ui.navigation.GenAIVisionRoute
import hu.bme.ait.generativeaidemo.ui.screen.genaitext.GenAIScreen
import hu.bme.ait.generativeaidemo.ui.screen.genaivision.GenAIVisionScreen
import hu.bme.ait.generativeaidemo.ui.theme.GenerativeAIDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GenerativeAIDemoTheme {
                AppEntryPoint()
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    // 2. Define separate back stacks for each tab to preserve their state
    //    "rememberNavBackStack" is the new primitive in Nav 3
    val genAITextBackStack = rememberNavBackStack(GenAITextRoute)
    val genAIVisionBackStack = rememberNavBackStack(GenAIVisionRoute)

    // 3. Track which tab is currently selected
    var selectedTab by rememberSaveable { mutableStateOf(CurrentScreen.GenAIText) }

    // 4. Select the active back stack based on the tab
    val currentBackStack = when (selectedTab) {
        CurrentScreen.GenAIText -> genAITextBackStack
        CurrentScreen.GenAIVision -> genAIVisionBackStack
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == CurrentScreen.GenAIText,
                    onClick = { selectedTab = CurrentScreen.GenAIText },
                    icon = { Icon(Icons.Default.Email, contentDescription = "TextAI") },
                    label = { Text("TextAI") }
                )
                // Profile Tab
                NavigationBarItem(
                    selected = selectedTab == CurrentScreen.GenAIVision,
                    onClick = { selectedTab = CurrentScreen.GenAIVision },
                    icon = { Icon(Icons.Default.Face, contentDescription = "VisionAI") },
                    label = { Text("VisionAI") }
                )
            }
        }
    ) { innerPadding ->
        // 5. NavDisplay replaces NavHost. It renders the 'backStack'.
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = currentBackStack,
            entryProvider  = entryProvider {
                entry<GenAITextRoute> {
                    GenAIScreen()
                }
                entry<GenAIVisionRoute> {
                    GenAIVisionScreen()
                }
            }
        )
    }
}
